package com.ssafy.trabuddy.domain.planShare.model.dto;

import com.ssafy.trabuddy.domain.member.model.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleRequest {
    private MemberRole role;
} 