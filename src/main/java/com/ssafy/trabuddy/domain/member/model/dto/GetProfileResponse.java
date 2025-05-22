package com.ssafy.trabuddy.domain.member.model.dto;

import com.ssafy.trabuddy.domain.member.model.enums.MemberSex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProfileResponse {
    private String nickname;
    private MemberSex sex;
    private int age;
}
