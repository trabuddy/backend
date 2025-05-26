package com.ssafy.trabuddy.domain.planShare.repository;

import com.ssafy.trabuddy.domain.planShare.model.entity.PlanShareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanShareRepository extends JpaRepository<PlanShareEntity, Long> {
    /**
     * 특정 사용자가 참여하고 있는 플랜 목록 조회 (탈퇴하지 않은 플랜만)
     */
    List<PlanShareEntity> findByMemberMemberIdAndLeftAtIsNull(Long memberId);
}
