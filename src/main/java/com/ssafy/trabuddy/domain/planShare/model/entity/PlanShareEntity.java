package com.ssafy.trabuddy.domain.planShare.model.entity;

import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "planShare")
public class PlanShareEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long planShareId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    @Setter
    private PlanEntity plan;


}
