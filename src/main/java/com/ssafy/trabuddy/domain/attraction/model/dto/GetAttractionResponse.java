package com.ssafy.trabuddy.domain.attraction.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAttractionResponse {
    private long attractionId;
    private String address1;
    private String address2;
    private double latitude;
    private double longitude;
    private String first_image_url;
    private String first_image_thumbnail_url;
}
