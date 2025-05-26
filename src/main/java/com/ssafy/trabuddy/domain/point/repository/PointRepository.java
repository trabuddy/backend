package com.ssafy.trabuddy.domain.point.repository;

import com.ssafy.trabuddy.domain.point.model.entity.PointEntity;
import com.ssafy.trabuddy.domain.point.repository.query.PointRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<PointEntity, Long>, PointRepositoryCustom {
    List<PointEntity> findAllByPlanPlanId(long planId);
}
