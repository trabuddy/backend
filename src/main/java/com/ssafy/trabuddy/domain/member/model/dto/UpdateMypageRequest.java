package com.ssafy.trabuddy.domain.member.model.dto;

import com.ssafy.trabuddy.domain.member.model.enums.MemberSex;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class UpdateMypageRequest {
    private String nickname;
    private Integer age;
    private MemberSex sex;
}
