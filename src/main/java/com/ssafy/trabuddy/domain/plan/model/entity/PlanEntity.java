package com.ssafy.trabuddy.domain.plan.model.entity;

import com.ssafy.trabuddy.domain.plan.enums.PlanVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plan")
@EntityListeners(AuditingEntityListener.class)
public class PlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long planId;

    //TODO: 테마 추가
    private int themeId;

    @CreatedBy
    private long ownerId;
    private String ownerNickname;

    private String title;
    private String description;
    private String memo;
    private int people;

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private PlanVisibility visibility;

    public PlanEntity(String ownerNickname) {
        this.ownerNickname = ownerNickname;
    }
}
