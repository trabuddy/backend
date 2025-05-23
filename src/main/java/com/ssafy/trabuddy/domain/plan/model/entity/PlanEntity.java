package com.ssafy.trabuddy.domain.plan.model.entity;

import com.ssafy.trabuddy.domain.plan.enums.PlanVisibility;
import com.ssafy.trabuddy.domain.planShare.model.entity.PlanShareEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Setter
    private int themeId;

    @CreatedBy
    private long ownerId;

    @Setter
    private String ownerNickname;

    @OneToMany(
            mappedBy = "plan",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PlanShareEntity> shares = new ArrayList<>();

    @Setter
    private String title;
    @Setter
    private String description;
    @Setter
    private String memo;
    @Setter
    private int people;

    @CreatedDate
    private LocalDateTime createdAt;
    @Setter
    private LocalDateTime deletedAt;

    @Setter
    private LocalDateTime startDate;
    @Setter
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private PlanVisibility visibility;

    public void addShare(PlanShareEntity share) {
        shares.add(share);
        share.setPlan(this);
    }

    public void removeShare(PlanShareEntity share) {
        shares.remove(share);
        share.setPlan(null);
    }
}
