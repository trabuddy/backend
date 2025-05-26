package com.ssafy.trabuddy.domain.planLike.service;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.member.repository.MemberRepository;
import com.ssafy.trabuddy.domain.plan.mapper.PlanMapper;
import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import com.ssafy.trabuddy.domain.plan.repository.PlanRepository;
import com.ssafy.trabuddy.domain.planLike.model.dto.PlanLikeResponse;
import com.ssafy.trabuddy.domain.planLike.model.entity.PlanLikeEntity;
import com.ssafy.trabuddy.domain.planLike.repository.PlanLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanLikeService {
    private final PlanLikeRepository planLikeRepository;
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;

    /**
     * 내가 좋아요 누른 플랜 목록 조회 (활성 상태만)
     */
    public List<GetPlanResponse> getMyLikedPlans(LoggedInMember loggedInMember) {
        log.info("PlanLikeService - getMyLikedPlans 시작");
        log.info(loggedInMember.toString());

        List<PlanLikeEntity> likedPlans = planLikeRepository.findByMemberMemberIdAndCanceledAtIsNull(loggedInMember.getMemberId());
        
        log.info("활성 좋아요 플랜 조회 - memberId: {}, count: {}", 
                loggedInMember.getMemberId(), likedPlans.size());
        
        return likedPlans.stream()
                .map(planLike -> PlanMapper.INSTANCE.toGetPlanResponse(planLike.getPlan()))
                .collect(Collectors.toList());
    }

    /**
     * 플랜 좋아요 토글 (현재 상태를 확인하여 자동 토글)
     * - 좋아요가 없으면 → 좋아요 추가 (liked_at 설정)
     * - 활성 좋아요가 있으면 → 좋아요 취소 (canceled_at 설정)
     * - 한 번 취소된 좋아요는 다시 누를 수 없음
     */
    @Transactional
    public PlanLikeResponse togglePlanLike(Long planId, LoggedInMember loggedInMember) {
        log.info("PlanLikeService - togglePlanLike 시작 - planId: {}, memberId: {}", 
                planId, loggedInMember.getMemberId());

        // 플랜 존재 확인
        PlanEntity plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("플랜을 찾을 수 없습니다: " + planId));

        // 멤버 엔티티 조회
        MemberEntity member = memberRepository.findById(loggedInMember.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + loggedInMember.getMemberId()));

        // 기존 좋아요 기록 확인 (활성/비활성 모두 포함)
        Optional<PlanLikeEntity> existingLike = planLikeRepository.findByPlanPlanIdAndMemberMemberId(planId, loggedInMember.getMemberId());

        boolean isLiked = false;
        String message = "";

        if (existingLike.isEmpty()) {
            // 좋아요 기록이 없음 → 새로 좋아요 추가
            PlanLikeEntity newLike = PlanLikeEntity.builder()
                    .plan(plan)
                    .member(member)
                    .build();
            planLikeRepository.save(newLike);
            isLiked = true;
            message = "좋아요를 눌렀습니다.";
            log.info("첫 좋아요 추가 - planId: {}, memberId: {}", planId, loggedInMember.getMemberId());
        } else {
            PlanLikeEntity like = existingLike.get();
            if (like.isActive()) {
                // 활성 좋아요가 있음 → 취소
                like.cancel();
                planLikeRepository.save(like);
                isLiked = false;
                message = "좋아요를 취소했습니다.";
                log.info("좋아요 취소 - planId: {}, memberId: {}, canceledAt: {}", 
                        planId, loggedInMember.getMemberId(), like.getCanceledAt());
            } else {
                // 취소된 좋아요가 있음 → 다시 누를 수 없음
                isLiked = false;
                message = "이미 좋아요를 취소한 플랜입니다. 다시 좋아요를 누를 수 없습니다.";
                log.info("취소된 좋아요 재시도 차단 - planId: {}, memberId: {}", planId, loggedInMember.getMemberId());
            }
        }

        return PlanLikeResponse.builder()
                .planId(planId)
                .liked(isLiked)
                .message(message)
                .build();
    }
} 