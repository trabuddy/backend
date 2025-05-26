package com.ssafy.trabuddy.domain.point.service;

import com.ssafy.trabuddy.domain.attraction.mapper.AttractionMapper;
import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import com.ssafy.trabuddy.domain.attraction.service.AttractionService;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import com.ssafy.trabuddy.domain.plan.service.PlanService;
import com.ssafy.trabuddy.domain.point.mapper.PointMapper;
import com.ssafy.trabuddy.domain.point.model.dto.*;
import com.ssafy.trabuddy.domain.point.model.entity.PointEntity;
import com.ssafy.trabuddy.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final AttractionService attractionService;
    private final PlanService planService;

    public AddPointResponse addPoint(long planId, AddPointRequest addPointRequest) {
        PointEntity point = PointMapper.INSTANCE.toPointEntity(addPointRequest);
        AttractionEntity attraction = attractionService.findById(addPointRequest.getAttractionId());
        PlanEntity plan = planService.findById(planId);

        point.setAttraction(attraction);
        point.setPlan(plan);

        PointEntity newPoint = pointRepository.save(point);
        return PointMapper.INSTANCE.toAddPointResponse(newPoint);
    }

    @Transactional
    public UpdatePointResponse updatePoint(long pointId, UpdatePointRequest updatePointRequest) {
        PointEntity point = pointRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("Point not found"));

        point.setEndDate(updatePointRequest.getTo());
        point.setStartDate(updatePointRequest.getFrom());

        return PointMapper.INSTANCE.toUpdatePointResponse(point);
    }

    @Transactional(readOnly = true)
    public List<GetPointResponse> getPointInPlan(long planId) {
        List<PointEntity> points = pointRepository.findAllByPlanPlanId(planId);
        return points
                .stream()
                .map(point -> GetPointResponse.builder()
                        .to(point.getEndDate())
                        .from(point.getStartDate())
                        .pointId(point.getPointId())
                        .attraction(AttractionMapper.INSTANCE.toGetAttractionResponse(point.getAttraction()))
                        .build())
                .toList();
    }

    @Transactional
    public void deletePoint(long pointId) {
        pointRepository.deleteById(pointId);
    }

    public PointEntity findByPointIdUsingFetchJoin(long pointId) {
        return pointRepository.findMemberById(pointId);
    }

    public long findMemberIdByPointId(long pointId) {
        return pointRepository.findById(pointId)
                .orElseThrow(() -> new IllegalArgumentException("경유지가 없는데요?"))
                .getPlan().getOwnerId();
    }
}
