package com.ssafy.trabuddy.domain.planShare.service;

import com.ssafy.trabuddy.domain.planShare.model.entity.PlanShareEntity;
import com.ssafy.trabuddy.domain.planShare.repository.PlanShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanShareService {
    private final PlanShareRepository planShareRepository;

    public PlanShareEntity save(PlanShareEntity planShareEntity) {
        return planShareRepository.save(planShareEntity);
    }
}
