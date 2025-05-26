package com.ssafy.trabuddy.domain.planShare.controller;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import com.ssafy.trabuddy.domain.plan.service.PlanService;
import com.ssafy.trabuddy.domain.planShare.model.dto.ParticipantResponse;
import com.ssafy.trabuddy.domain.planShare.model.dto.UpdateNicknameRequest;
import com.ssafy.trabuddy.domain.planShare.model.dto.UpdateNicknameResponse;
import com.ssafy.trabuddy.domain.planShare.model.dto.UpdateRoleRequest;
import com.ssafy.trabuddy.domain.planShare.model.dto.UpdateRoleResponse;
import com.ssafy.trabuddy.domain.planShare.service.PlanShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PlanParticipantController {
    private final PlanService planService;
    private final PlanShareService planShareService;

    /**
     * 참여자 정보 받아오기
     */
    @GetMapping("/v1/plans/{planId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable Long planId) {
        PlanEntity plan = planService.findById(planId);
        List<ParticipantResponse> participants = planShareService.getParticipants(plan);
        return ResponseEntity.ok(participants);
    }

    /**
     * 주최자는 참여자들의 권한을 설정할 수 있다
     */
    @PutMapping("/v1/plans/{planId}/participants/{userId}")
    public ResponseEntity<UpdateRoleResponse> updateParticipantRole(
            @PathVariable Long planId,
            @PathVariable Long userId,
            @RequestBody UpdateRoleRequest request,
            @AuthenticationPrincipal LoggedInMember loggedInMember) {
        
        PlanEntity plan = planService.findById(planId);
        UpdateRoleResponse response = planShareService.updateParticipantRole(plan, userId, request.getRole(), loggedInMember);
        return ResponseEntity.ok(response);
    }

    /**
     * 주최자는 참여자를 추방할 수 있다
     */
    @DeleteMapping("/v1/plans/{planId}/participants/{userId}")
    public ResponseEntity<Void> removeParticipant(
            @PathVariable Long planId,
            @PathVariable Long userId,
            @AuthenticationPrincipal LoggedInMember loggedInMember) {
        
        PlanEntity plan = planService.findById(planId);
        planShareService.removeParticipant(plan, userId, loggedInMember);
        return ResponseEntity.ok().build();
    }

    /**
     * 참여자는 플랜 내에서의 닉네임을 변경할 수 있다
     */
    @PutMapping("/v1/plans/{planId}/nickname/{userId}")
    public ResponseEntity<UpdateNicknameResponse> updateNickname(
            @PathVariable Long planId,
            @PathVariable Long userId,
            @RequestBody UpdateNicknameRequest request,
            @AuthenticationPrincipal LoggedInMember loggedInMember) {
        
        PlanEntity plan = planService.findById(planId);
        UpdateNicknameResponse response = planShareService.updateNickname(plan, userId, request.getNickname(), loggedInMember);
        return ResponseEntity.ok(response);
    }

    /**
     * 참여자는 플랜을 탈퇴할 수 있다
     */
    @DeleteMapping("/v1/plans/{planId}")
    public ResponseEntity<Void> leavePlan(
            @PathVariable Long planId,
            @AuthenticationPrincipal LoggedInMember loggedInMember) {
        
        PlanEntity plan = planService.findById(planId);
        planShareService.leavePlan(plan, loggedInMember);
        return ResponseEntity.ok().build();
    }
} 