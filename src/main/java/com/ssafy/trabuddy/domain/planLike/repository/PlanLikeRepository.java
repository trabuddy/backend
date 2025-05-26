package com.ssafy.trabuddy.domain.planLike.repository;

import com.ssafy.trabuddy.domain.planLike.model.entity.PlanLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanLikeRepository extends JpaRepository<PlanLikeEntity, Long> {
    /**
     * 특정 사용자가 좋아요 누른 플랜 목록 조회 (활성 상태만)
     */
    List<PlanLikeEntity> findByMemberMemberIdAndCanceledAtIsNull(Long memberId);

    /**
     * 특정 플랜에 특정 사용자가 활성 좋아요를 눌렀는지 확인
     */
    boolean existsByPlanPlanIdAndMemberMemberIdAndCanceledAtIsNull(Long planId, Long memberId);

    /**
     * 특정 플랜에 특정 사용자의 좋아요 제거
     */
    void deleteByPlanPlanIdAndMemberMemberId(Long planId, Long memberId);

    /**
     * 특정 플랜에 특정 사용자의 좋아요 엔티티 조회 (활성/비활성 모두 포함)
     */
    Optional<PlanLikeEntity> findByPlanPlanIdAndMemberMemberId(Long planId, Long memberId);

    /**
     * 특정 플랜에 특정 사용자의 활성 좋아요 엔티티 조회
     */
    Optional<PlanLikeEntity> findByPlanPlanIdAndMemberMemberIdAndCanceledAtIsNull(Long planId, Long memberId);
} 