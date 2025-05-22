package com.ssafy.trabuddy.domain.member.controller;

import com.ssafy.trabuddy.domain.member.model.dto.GetNicknameResponse;
import com.ssafy.trabuddy.domain.member.model.dto.GetProfileResponse;
import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.member.model.dto.UpdateMypageRequest;
import com.ssafy.trabuddy.domain.member.service.MemberService;
import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/v1/users/profile")
    public ResponseEntity<GetNicknameResponse> getNickname(@AuthenticationPrincipal LoggedInMember loggedInMember) {
        GetNicknameResponse getNicknameResponse = memberService.getNickname(loggedInMember);
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
