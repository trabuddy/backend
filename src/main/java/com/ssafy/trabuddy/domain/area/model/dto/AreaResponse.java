package com.ssafy.trabuddy.domain.area.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.trabuddy.domain.sigungu.model.dto.AreaSigunguResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class AreaResponse implements AreaSigunguResponse {
    private int areaCode;
    private String name;
    @JsonIgnore
    private int sigunguCode;
}
