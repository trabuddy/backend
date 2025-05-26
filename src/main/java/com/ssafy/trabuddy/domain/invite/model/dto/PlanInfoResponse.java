package com.ssafy.trabuddy.domain.invite.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanInfoResponse {
    private String nickname; // 주최자 닉네임
    private String title;    // 플랜 제목
} 