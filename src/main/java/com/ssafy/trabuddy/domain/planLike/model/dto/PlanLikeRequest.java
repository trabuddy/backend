package com.ssafy.trabuddy.domain.planLike.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlanLikeRequest {
    private String like; // "yes" or "no"
} 