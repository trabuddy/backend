package com.ssafy.trabuddy.domain.plan.repository;

import com.ssafy.trabuddy.domain.plan.enums.PlanVisibility;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {
    List<PlanEntity> findByDeletedAtIsNullAndOwnerId(long ownerId);
    
    /**
     * 특정 visibility와 삭제되지 않은 플랜 조회
     */
    List<PlanEntity> findByVisibilityAndDeletedAtIsNull(PlanVisibility visibility);
}
