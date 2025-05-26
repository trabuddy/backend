package com.ssafy.trabuddy.domain.plan.model.dto;

import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanShareResponse {
    private long memberId;
    private String nickname;
    private MemberRole role;
    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;
} 