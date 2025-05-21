package com.ssafy.trabuddy.domain.sigungu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class SigunguResponse implements AreaSigunguResponse {
    private int sigunguCode;
    private int areaCode;
    private String name;

}
