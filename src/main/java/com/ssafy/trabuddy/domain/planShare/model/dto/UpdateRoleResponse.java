package com.ssafy.trabuddy.domain.planShare.model.dto;

import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleResponse {
    private Long userId;
    private MemberRole role;
} 