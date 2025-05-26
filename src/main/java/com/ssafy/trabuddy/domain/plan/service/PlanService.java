package com.ssafy.trabuddy.domain.plan.service;

import com.ssafy.trabuddy.common.util.NullPropertyUtils;
import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import com.ssafy.trabuddy.domain.member.repository.MemberRepository;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanService {
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final PlanShareService planShareService;

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
}
