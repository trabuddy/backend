package com.ssafy.trabuddy.domain.plan.controller;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import com.ssafy.trabuddy.domain.plan.model.dto.AddPlanRequest;
import com.ssafy.trabuddy.domain.plan.model.dto.GetPlanResponse;
import com.ssafy.trabuddy.domain.plan.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    @PostMapping("/v1/plans")
    public ResponseEntity<GetPlanResponse> getPlan(@Valid @RequestBody AddPlanRequest request, @AuthenticationPrincipal LoggedInMember loggedInMember) {
        GetPlanResponse response = planService.addPlan(request, loggedInMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/plans/{planId}")
    public ResponseEntity<GetPlanResponse> getPlan(@PathVariable long planId) {
        GetPlanResponse response = planService.getPlan(planId);
        return ResponseEntity.ok(response);
    }

}
