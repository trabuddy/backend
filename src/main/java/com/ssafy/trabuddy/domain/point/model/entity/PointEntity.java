package com.ssafy.trabuddy.domain.point.model.entity;

import com.ssafy.trabuddy.domain.attraction.model.entity.AttractionEntity;
import com.ssafy.trabuddy.domain.plan.model.entity.PlanEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "point")
public class PointEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    @Setter
    private PlanEntity plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attraction_id")
    @Setter
    private AttractionEntity attraction;

    @Setter
    private LocalDateTime startDate;
    @Setter
    private LocalDateTime endDate;

    @Builder
    public PointEntity(LocalDateTime startDate, LocalDateTime endDate, AttractionEntity attraction) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.attraction = attraction;
    }


}
