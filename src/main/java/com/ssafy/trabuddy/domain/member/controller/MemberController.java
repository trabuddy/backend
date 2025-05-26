package com.ssafy.trabuddy.domain.member.controller;

import com.ssafy.trabuddy.domain.member.model.dto.GetMemberInfoResponse;
import com.ssafy.trabuddy.domain.member.model.dto.GetProfileResponse;
import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.model.dto.UpdateMypageRequest;
import com.ssafy.trabuddy.domain.member.service.MemberService;
import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import com.ssafy.trabuddy.domain.planLike.service.PlanLikeService;
import com.ssafy.trabuddy.domain.planShare.service.PlanShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final PlanShareService planShareService;
    private final PlanLikeService planLikeService;

    /**
     * 프로필에서 내가 참여하는 플랜을 볼 수 있다
     */
    @GetMapping("/v1/users/my-joins")
    public ResponseEntity<List<GetPlanResponse>> getMyJoinedPlans(
            @AuthenticationPrincipal LoggedInMember loggedInMember) {

        List<GetPlanResponse> joinedPlans = planShareService.getMyJoinedPlans();
        return ResponseEntity.ok(joinedPlans);
    }

    /**
     * 내가 좋아요 누른 플랜들을 볼 수 있다
     */
    @GetMapping("/v1/users/likes")
    public ResponseEntity<List<GetPlanResponse>> getMyLikedPlans(
            @AuthenticationPrincipal LoggedInMember loggedInMember) {

        log.info("Controller - getMyLikedPlans 시작");
        
        // SecurityContext 직접 확인
        var securityContext = SecurityContextHolder.getContext();
        var authentication = securityContext.getAuthentication();
        
        log.info("Controller - SecurityContext: {}", securityContext);
        log.info("Controller - Authentication: {}", authentication);
        
        if (authentication != null) {
            log.info("Controller - Authentication isAuthenticated: {}", authentication.isAuthenticated());
            log.info("Controller - Principal type: {}", authentication.getPrincipal().getClass().getName());
            log.info("Controller - Principal value: {}", authentication.getPrincipal());
        }
        
        log.info("Controller - @AuthenticationPrincipal LoggedInMember: {}", loggedInMember);
        
        if (loggedInMember == null) {
            // SecurityContext에서 직접 가져와서 사용
            if (authentication != null && authentication.getPrincipal() instanceof LoggedInMember) {
                loggedInMember = (LoggedInMember) authentication.getPrincipal();
                log.info("Controller - SecurityContext에서 직접 가져온 LoggedInMember: {}", loggedInMember);
            } else {
                log.error("Controller - LoggedInMember를 찾을 수 없습니다.");
                throw new IllegalStateException("로그인이 필요합니다.");
            }
        }

        List<GetPlanResponse> likedPlans = planLikeService.getMyLikedPlans(loggedInMember);
        return ResponseEntity.ok(likedPlans);
    }

    @GetMapping("/v1/users/profile")
    public ResponseEntity<GetMemberInfoResponse> getNickname(@AuthenticationPrincipal LoggedInMember loggedInMember) {
        GetMemberInfoResponse getNicknameResponse = memberService.getNickname(loggedInMember);
        return ResponseEntity.ok(getNicknameResponse);
    }

    @GetMapping("/v1/users/my-page")
    public ResponseEntity<GetProfileResponse> getProfile(@AuthenticationPrincipal LoggedInMember loggedInMember) {
        GetProfileResponse getProfileResponse = memberService.getProfile(loggedInMember);
        return ResponseEntity.ok(getProfileResponse);
    }

    @GetMapping("/v1/users/my-plans")
    public ResponseEntity<List<GetPlanResponse>> getPlans(@AuthenticationPrincipal LoggedInMember loggedInMember) {
        List<GetPlanResponse> getPlanResponses = memberService.getPlans(loggedInMember);
        return ResponseEntity.ok(getPlanResponses);
    }

    @PutMapping("/v1/users/my-page")
    public ResponseEntity<Void> updateMyPage(@AuthenticationPrincipal LoggedInMember loggedInMember, @RequestBody UpdateMypageRequest request) {
        memberService.updateMypage(request, loggedInMember);
        log.info(request.toString());
        return ResponseEntity.ok().build();
    }
}
