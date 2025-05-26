package com.ssafy.trabuddy.domain.planLike.model.entity;

import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "plan_like", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"plan_id", "member_id"})
})
@EntityListeners(AuditingEntityListener.class)
public class PlanLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private PlanEntity plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @CreatedDate
    private LocalDateTime likedAt;

    @Setter
    private LocalDateTime canceledAt;

    /**
     * 좋아요가 활성 상태인지 확인 (취소되지 않았는지)
     */
    public boolean isActive() {
        return canceledAt == null;
    }

    /**
     * 좋아요 취소
     */
    public void cancel() {
        this.canceledAt = LocalDateTime.now();
    }
} 