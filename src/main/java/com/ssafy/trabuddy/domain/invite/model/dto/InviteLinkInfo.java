package com.ssafy.trabuddy.domain.invite.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InviteLinkInfo {
    private Long planId;
    private String inviteCode;
    private Long createdBy;
    private LocalDateTime createdAt;
    private Long remainingTTL; // 남은 TTL (초 단위)
} 