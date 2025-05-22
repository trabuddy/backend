package com.ssafy.trabuddy.domain.plan.service;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.member.repository.MemberRepository;
import com.ssafy.trabuddy.domain.member.service.MemberService;
import com.ssafy.trabuddy.domain.plan.mapper.PlanMapper;
import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import com.ssafy.trabuddy.domain.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanService {
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;


    public GetPlanResponse addPlan(LoggedInMember loggedInMember) {
        PlanEntity planEntity = planRepository.save(new PlanEntity(loggedInMember.getNickname()));
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
