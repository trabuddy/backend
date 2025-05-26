package com.ssafy.trabuddy.domain.invite.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinPlanRequest {
    private String inviteCode;
} 