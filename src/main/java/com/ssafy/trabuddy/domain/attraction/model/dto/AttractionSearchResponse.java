package com.ssafy.trabuddy.domain.attraction.model.dto;

import com.ssafy.trabuddy.domain.area.model.entity.AreaEntity;
import com.ssafy.trabuddy.domain.sigungu.model.entity.SigunguEntity;
import lombok.*;

import com.ssafy.trabuddy.domain.attraction.model.enums.AttractionSource;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AttractionSearchResponse {
    private long id;

    private String contentId;
    private String contentTypeId;
    private String title;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private String telephone;
    private String address1;
    private String address2;
    private String zipCode;
    private String category1;
    private String category2;
    private String category3;
    private double latitude;
    private double longitude;
    private String mapLevel;
    private String firstImageUrl;
    private String firstImageThumbnailUrl;
    private String copyrightDivisionCode;
    private String booktourInfo;

    private AttractionSource source;

    private SigunguEntity sigungu;

    private AreaEntity area;
}
