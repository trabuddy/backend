package com.ssafy.trabuddy.domain.point.controller;

import com.ssafy.trabuddy.domain.point.model.dto.*;
import com.ssafy.trabuddy.domain.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @PostMapping("/v1/plans/{planId}/points")
    public ResponseEntity<AddPointResponse> addPoint(@PathVariable long planId,
                                                     @RequestBody AddPointRequest addPointRequest) {
        AddPointResponse addPointResponse = pointService.addPoint(planId, addPointRequest);
        return ResponseEntity.ok(addPointResponse);
    }

    @PatchMapping("/v1/plans/{planId}/points/{pointId}")
    public ResponseEntity<UpdatePointResponse> updatePoint(@PathVariable long pointId, @RequestBody UpdatePointRequest request) {
        UpdatePointResponse response = pointService.updatePoint(pointId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/plans/{planId}/points")
    public ResponseEntity<List<GetPointResponse>> getPoints(@PathVariable long planId) {
        List<GetPointResponse> responses = pointService.getPointInPlan(planId);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/v1/plans/{planId}/points/{pointId}")
    public ResponseEntity<Void> deletePoint(@PathVariable long pointId) {
        pointService.deletePoint(pointId);
        return ResponseEntity.noContent().build();
    }
}
