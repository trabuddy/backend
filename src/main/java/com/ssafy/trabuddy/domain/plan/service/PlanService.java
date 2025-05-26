package com.ssafy.trabuddy.domain.plan.service;

import com.ssafy.trabuddy.common.util.NullPropertyUtils;
import com.ssafy.trabuddy.domain.attraction.mapper.AttractionMapper;
import com.ssafy.trabuddy.domain.attraction.model.dto.AttractionSearchResponse;
import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import com.ssafy.trabuddy.domain.attraction.model.enums.AttractionSource;
import com.ssafy.trabuddy.domain.attraction.repository.AttractionRepository;
import com.ssafy.trabuddy.domain.kakaoPlaceSearch.model.dto.KakaoPlaceSearchResponse;
import com.ssafy.trabuddy.domain.kakaoPlaceSearch.service.KakaoPlaceSearchService;
import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import com.ssafy.trabuddy.domain.member.repository.MemberRepository;
import com.ssafy.trabuddy.domain.plan.enums.PlanVisibility;
import com.ssafy.trabuddy.domain.plan.error.PlanErrorCode;
import com.ssafy.trabuddy.domain.plan.error.PlanNotFoundException;
import com.ssafy.trabuddy.domain.plan.mapper.PlanMapper;
import com.ssafy.trabuddy.domain.plan.model.dto.AddPlanRequest;
import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import com.ssafy.trabuddy.domain.plan.model.dto.UpdatePlanRequest;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import com.ssafy.trabuddy.domain.plan.repository.PlanRepository;
import com.ssafy.trabuddy.domain.planShare.model.entity.PlanShareEntity;
import com.ssafy.trabuddy.domain.planShare.service.PlanShareService;
import com.ssafy.trabuddy.domain.recommendation.service.RecommendationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanService {
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final PlanShareService planShareService;
    private final AttractionRepository attractionRepository;
    private final RecommendationService recommendationService;
    private final KakaoPlaceSearchService kakaoPlaceSearchService;

    public GetPlanResponse addPlan(AddPlanRequest addPlanRequest, LoggedInMember loggedInMembers) {
        PlanEntity planEntity = planRepository.save(PlanMapper.INSTANCE.toPlanEntity(addPlanRequest, loggedInMembers));
        MemberEntity member = memberRepository.findByMemberIdAndUnregisteredAtIsNull(loggedInMembers.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("없는 회원입니다."));

        PlanShareEntity planShare = PlanShareEntity.builder()
                .member(member)
                .plan(planEntity)
                .nickname(loggedInMembers.getNickname())
                .role(MemberRole.owner)
                .build();

        PlanShareEntity newPlanShare = planShareService.save(planShare);
        planEntity.addShare(newPlanShare);

        return PlanMapper.INSTANCE.toGetPlanResponse(planEntity);
    }

    public GetPlanResponse getPlan(long planId) {
        PlanEntity planEntity = planRepository
                .findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(PlanErrorCode.NOT_FOUND_PLAN));
        return PlanMapper.INSTANCE.toGetPlanResponse(planEntity);
    }

    public List<GetPlanResponse> getPlans(LoggedInMember loggedInMember) {
        List<GetPlanResponse> planResponses = planRepository
                .findByDeletedAtIsNullAndOwnerId(loggedInMember.getMemberId())
                .stream()
                .map(PlanMapper.INSTANCE::toGetPlanResponse)
                .toList();

        log.info(planResponses.toString());
        return planResponses;
    }

    /**
     * 공개 플랜 목록 조회 (OPEN 상태인 다른 사람의 플랜)
     */
    public List<GetPlanResponse> getOpenPlans() {
        log.info("PlanService - getOpenPlans 시작");

        List<PlanEntity> openPlans = planRepository.findByVisibilityAndDeletedAtIsNull(PlanVisibility.open);

        log.info("공개 플랜 조회 완료 - count: {}", openPlans.size());

        return openPlans.stream()
                .map(PlanMapper.INSTANCE::toGetPlanResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public GetPlanResponse updatePlan(long planId, UpdatePlanRequest request) {
        PlanEntity plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(PlanErrorCode.NOT_FOUND_PLAN));

        String[] ignoreProperties = NullPropertyUtils.getNullPropertyNames(request);

        BeanUtils.copyProperties(request, plan, ignoreProperties);

        return PlanMapper.INSTANCE.toGetPlanResponse(plan);
    }

    public PlanEntity findById(long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(PlanErrorCode.NOT_FOUND_PLAN));
    }

    public List<AttractionSearchResponse> getRecommendedAttractions(long planId) {
        // 1. planRepository를 통해 plan 정보 가져오기
        PlanEntity plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(PlanErrorCode.NOT_FOUND_PLAN));

        // 2. RecommendationService의 함수에 파라미터 넣어서 추천 여행지 목록 받기
        List<RecommendationService.Recommendation> recommendations = recommendationService.recommend(
                10, // 추천 개수 (기본값 10개)
                plan.getPeople(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.getThemeId()
        );

        List<AttractionEntity> attractionEntities = new ArrayList<>();

        // 3. 각 추천 여행지에 대해 카카오 장소 검색 실행
        for (RecommendationService.Recommendation recommendation : recommendations) {
            try {
                // 카카오 장소 검색 실행
                KakaoPlaceSearchResponse kakaoResponse = kakaoPlaceSearchService.searchPlacesByKeyword(
                        URLEncoder.encode(recommendation.name(), "UTF-8"));

                // 검색 결과가 있는 경우
                if (kakaoResponse != null &&
                    kakaoResponse.getDocuments() != null &&
                    !kakaoResponse.getDocuments().isEmpty()) {

                    KakaoPlaceSearchResponse.Document document = kakaoResponse.getDocuments().get(0);

                    // 4. attraction 테이블에서 중복 확인
                    Optional<AttractionEntity> existingAttraction = attractionRepository
                            .findByContentIdAndSource(document.getId(), AttractionSource.kakao);

                    AttractionEntity attractionEntity;

                    if (existingAttraction.isPresent()) {
                        // 이미 존재하는 경우 기존 엔티티 사용
                        attractionEntity = existingAttraction.get();
                    } else {
                        // 존재하지 않는 경우 새로운 AttractionEntity 생성 및 저장
                        // MapStruct를 사용하여 Document를 AttractionEntity로 변환
                        attractionEntity = AttractionMapper.INSTANCE.toAttractionEntity(document);

                        // 새로운 엔티티를 데이터베이스에 저장
                        attractionEntity = attractionRepository.save(attractionEntity);
                    }

                    attractionEntities.add(attractionEntity);
                }
            } catch (Exception e) {
                log.warn("카카오 장소 검색 중 오류 발생: {}, 추천지: {}", e.getMessage(), recommendation.name());
                // 오류가 발생한 경우 해당 추천지는 스킵하고 계속 진행
            }
        }

        // 5. AttractionEntity 목록을 AttractionSearchResponse 목록으로 변환하여 반환
        return attractionEntities.stream()
                .map(AttractionMapper.INSTANCE::toAttractionSearchResponse)
                .toList();
    }
}
