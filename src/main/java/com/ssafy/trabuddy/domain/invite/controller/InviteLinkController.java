package com.ssafy.trabuddy.domain.invite.controller;

import com.ssafy.trabuddy.domain.invite.model.dto.InviteLinkResponse;
import com.ssafy.trabuddy.domain.invite.model.dto.JoinPlanRequest;
import com.ssafy.trabuddy.domain.invite.model.dto.JoinPlanResponse;
import com.ssafy.trabuddy.domain.invite.model.dto.PlanInfoResponse;
import com.ssafy.trabuddy.domain.invite.service.InviteLinkService;
import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import com.ssafy.trabuddy.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InviteLinkController {
    private final InviteLinkService inviteLinkService;
    private final PlanService planService;

    /**
     * 초대 링크 생성 (주최자만 가능)
     * 인터셉터에서 주최자 권한 체크 완료
     */
    @PostMapping("/v1/plans/{planId}/invite")
    public ResponseEntity<InviteLinkResponse> createInviteLink(@PathVariable long planId) {
        PlanEntity plan = planService.findById(planId);
        InviteLinkResponse response = inviteLinkService.createInviteLink(plan);
        log.info("초대 링크 생성 - planId: {}", planId);
        return ResponseEntity.ok(response);
    }

    /**
     * 참여 코드에 해당하는 hostname, planName 받기
     */
    @GetMapping("/v1/plans/join")
    public ResponseEntity<PlanInfoResponse> getPlanInfo(@RequestParam String inviteCode) {
        PlanInfoResponse response = inviteLinkService.getPlanInfo(inviteCode);
        return ResponseEntity.ok(response);
    }

    /**
     * 초대 코드로 플랜 참여
     */
    @PostMapping("/v1/plans/join")
    public ResponseEntity<JoinPlanResponse> joinPlan(
            @RequestBody JoinPlanRequest request,
            @AuthenticationPrincipal LoggedInMember loggedInMember) {
        
        if (request.getInviteCode() == null || request.getInviteCode().trim().isEmpty()) {
            throw new IllegalArgumentException("초대 코드가 필요합니다.");
        }
        
        JoinPlanResponse response = inviteLinkService.joinPlan(request.getInviteCode(), loggedInMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/invite/validate/{inviteCode}")
    public ResponseEntity<Long> validateInviteLink(@PathVariable String inviteCode) {
        Long planId = inviteLinkService.validateInviteLink(inviteCode);
        return ResponseEntity.ok(planId);
    }

    @DeleteMapping("/v1/invite/{inviteCode}")
    public ResponseEntity<Void> deleteInviteLink(@PathVariable String inviteCode) {
        inviteLinkService.deleteInviteLink(inviteCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/invite/ttl/{inviteCode}")
    public ResponseEntity<Long> getInviteLinkTTL(@PathVariable String inviteCode) {
        Long remainingTTL = inviteLinkService.getRemainingTTL(inviteCode);
        return ResponseEntity.ok(remainingTTL);
    }
} 