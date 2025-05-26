package com.ssafy.trabuddy.domain.kakaoPlaceSearch.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KakaoPlaceSearchResponse {
    private Meta meta;
    private List<Document> documents;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Meta {
        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("pageable_count")
        private int pageableCount;

        @JsonProperty("is_end")
        private boolean isEnd;

        @JsonProperty("same_name")
        private SameName sameName;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class SameName {
        private List<String> region;
        private String keyword;

        @JsonProperty("selected_region")
        private String selectedRegion;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Document {
        @JsonProperty("place_name")
        private String placeName;

        private String distance;

        @JsonProperty("place_url")
        private String placeUrl;

        @JsonProperty("category_name")
        private String categoryName;

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("road_address_name")
        private String roadAddressName;

        private String id;

        private String phone;

        @JsonProperty("category_group_code")
        private String categoryGroupCode;

        @JsonProperty("category_group_name")
        private String categoryGroupName;

        private String x;  // longitude
        private String y;  // latitude
    }
}
