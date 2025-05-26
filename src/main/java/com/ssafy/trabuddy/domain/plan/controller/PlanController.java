package com.ssafy.trabuddy.domain.plan.controller;

import com.ssafy.trabuddy.domain.attraction.model.dto.AttractionSearchResponse;
import com.ssafy.trabuddy.domain.kakaoPlaceSearch.model.dto.KakaoPlaceSearchResponse;
import com.ssafy.trabuddy.domain.kakaoPlaceSearch.service.KakaoPlaceSearchService;
import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.plan.model.dto.AddPlanRequest;
import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import com.ssafy.trabuddy.domain.plan.model.dto.UpdatePlanRequest;
import com.ssafy.trabuddy.domain.plan.service.PlanService;
import com.ssafy.trabuddy.domain.planLike.model.dto.PlanLikeResponse;
import com.ssafy.trabuddy.domain.planLike.service.PlanLikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PlanController {
    private final PlanService planService;
    private final PlanLikeService planLikeService;
    private final KakaoPlaceSearchService kakaoPlaceSearchService;

    @PostMapping("/v1/plans")
    public ResponseEntity<GetPlanResponse> addPlan(@Valid @RequestBody AddPlanRequest request, @AuthenticationPrincipal LoggedInMember loggedInMember) {
        GetPlanResponse response = planService.addPlan(request, loggedInMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/plans/{planId}")
    public ResponseEntity<GetPlanResponse> getPlan(@PathVariable long planId) {
        GetPlanResponse response = planService.getPlan(planId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/v1/plans/{planId}")
    public ResponseEntity<GetPlanResponse> updatePlan(@PathVariable long planId, @Valid @RequestBody UpdatePlanRequest request) {
        GetPlanResponse response = planService.updatePlan(planId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 공개 플랜 목록 조회 (OPEN 상태인 다른 사람의 플랜)
     */
    @GetMapping("/v1/plans")
    public ResponseEntity<List<GetPlanResponse>> getOpenPlans() {
        log.info("PlanController - getOpenPlans 요청");
        List<GetPlanResponse> response = planService.getOpenPlans();
        return ResponseEntity.ok(response);
    }

    /**
     * 플랜 좋아요/취소 토글
     * 현재 좋아요 상태를 확인하여 자동으로 토글 (좋아요 ↔ 취소)
     */
    @PostMapping("/v1/plans/{planId}/like")
    public ResponseEntity<PlanLikeResponse> togglePlanLike(@PathVariable Long planId) {
        log.info("PlanController - togglePlanLike 요청 - planId: {}", planId);

        // SecurityContext에서 인증된 사용자 정보 가져오기
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoggedInMember)) {
            log.error("인증되지 않은 사용자의 좋아요 요청");
            return ResponseEntity.status(401).build();
        }

        LoggedInMember loggedInMember = (LoggedInMember) authentication.getPrincipal();
        PlanLikeResponse response = planLikeService.togglePlanLike(planId, loggedInMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/plans/{planId}/recommendation")
    public ResponseEntity<List<AttractionSearchResponse>> getRecommendedAttractions(@PathVariable long planId, @AuthenticationPrincipal LoggedInMember loggedInMember) {
        List<AttractionSearchResponse> response = planService.getRecommendedAttractions(planId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/plans/search-place")
    public ResponseEntity<KakaoPlaceSearchResponse> searchPlace(@RequestParam String query) {
        KakaoPlaceSearchResponse response = kakaoPlaceSearchService.searchPlacesByKeyword(query);
        return ResponseEntity.ok(response);
    }
}
