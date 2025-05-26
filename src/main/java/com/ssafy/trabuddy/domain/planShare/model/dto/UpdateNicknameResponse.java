package com.ssafy.trabuddy.domain.planShare.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateNicknameResponse {
    private Long userId;
    private String nickname;
} 