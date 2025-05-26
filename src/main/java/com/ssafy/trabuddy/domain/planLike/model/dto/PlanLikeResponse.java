package com.ssafy.trabuddy.domain.planLike.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanLikeResponse {
    private Long planId;
    private boolean liked;
    private String message;
} 