package com.ssafy.trabuddy.domain.kakaoPlaceSearch.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPlaceSearchRequest {
    private String query;           // 검색을 원하는 질의어
    private String categoryGroupCode; // 카테고리 그룹 코드
    private String x;               // 중심 좌표의 X값 (경도)
    private String y;               // 중심 좌표의 Y값 (위도)
    private Integer radius;         // 반경 (미터)
    private String rect;            // 사각형 범위
    private Integer page;           // 결과 페이지 번호
    private Integer size;           // 한 페이지에 보여질 문서 수
    private String sort;            // 정렬 옵션 (distance 또는 accuracy)
}
