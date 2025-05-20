package com.ssafy.trabuddy.domain.attraction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttractionSearchRequest {
    private String areaCode;
    private String sigunguCode;
    private String keyword;
}
