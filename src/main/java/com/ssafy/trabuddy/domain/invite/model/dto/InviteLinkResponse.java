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
public class InviteLinkResponse {
    private String inviteCode;
    private LocalDateTime expiresAt;
} 