package com.ssafy.trabuddy.domain.plan.model.dto;

import com.ssafy.trabuddy.domain.plan.enums.PlanVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPlanResponse {
    private long planId;
    private int themeId;
    private long owenerId;
    private String ownerNickname;
    private String title;
    private String description;
    private String memo;

    private LocalDateTime createdAt;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private PlanVisibility visibility;
    
//    private List<PlanShareResponse> participants;
}
