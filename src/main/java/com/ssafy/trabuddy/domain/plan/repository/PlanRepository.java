package com.ssafy.trabuddy.domain.plan.repository;

import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {
    List<PlanEntity> findByDeletedAtIsNullAndOwnerId(long ownerId);
}
