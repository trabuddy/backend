package com.ssafy.trabuddy.domain.plan.service;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.repository.MemberRepository;
import com.ssafy.trabuddy.domain.plan.error.PlanErrorCode;
import com.ssafy.trabuddy.domain.plan.error.PlanNotFoundException;
import com.ssafy.trabuddy.domain.plan.mapper.PlanMapper;
import com.ssafy.trabuddy.domain.plan.model.dto.AddPlanRequest;
import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import com.ssafy.trabuddy.domain.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanService {
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;


    public GetPlanResponse addPlan(AddPlanRequest addPlanRequest, LoggedInMember loggedInMembers) {
        PlanEntity planEntity = planRepository.save(PlanMapper.INSTANCE.toPlanEntity(addPlanRequest, loggedInMembers));
        return PlanMapper.INSTANCE.toGetPlanResponse(planEntity);
    }

    public GetPlanResponse getPlan(long planId) {
        PlanEntity planEntity = planRepository
                .findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(PlanErrorCode.NOT_FOUND_PLAN));
        return PlanMapper.INSTANCE.toGetPlanResponse(planEntity);
    }

    public List<GetPlanResponse> getPlans(LoggedInMember loggedInMember) {
        List<GetPlanResponse> planResponses = planRepository.findByDeletedAtIsNullAndOwnerId(loggedInMember.getMemberId())
                .stream()
                .map(PlanMapper.INSTANCE::toGetPlanResponse)
                .toList();

        log.info(planResponses.toString());
        return planResponses;
    }

//    public GetPlanResponse getPlan(LoggedInMember loggedInMember) {
//
//    }
}
