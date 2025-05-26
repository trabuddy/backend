package com.ssafy.trabuddy.domain.planShare.model.entity;

import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plan_share")
public class PlanShareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long planShareId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    @Setter
    private PlanEntity plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Setter
    private MemberEntity member;

    @CreatedDate
    private LocalDateTime joinedAt;
    @Setter
    private LocalDateTime leftAt;

    @Setter
    private String nickname;
    @Enumerated(EnumType.STRING)
    @Setter
    private MemberRole role;

    @Builder
    public PlanShareEntity(PlanEntity plan, MemberEntity member, String nickname, MemberRole role) {
        this.plan = plan;
        this.member = member;
        this.nickname = nickname;
        this.role = role;
    }
}