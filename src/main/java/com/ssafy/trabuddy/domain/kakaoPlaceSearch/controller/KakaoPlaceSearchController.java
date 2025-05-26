package com.ssafy.trabuddy.domain.kakaoPlaceSearch.controller;

import com.ssafy.trabuddy.domain.kakaoPlaceSearch.model.dto.KakaoPlaceSearchRequest;
import com.ssafy.trabuddy.domain.kakaoPlaceSearch.model.dto.KakaoPlaceSearchResponse;
import com.ssafy.trabuddy.domain.kakaoPlaceSearch.service.KakaoPlaceSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/kakao-place-search")
@RequiredArgsConstructor
@Slf4j
public class KakaoPlaceSearchController {

    private final KakaoPlaceSearchService kakaoPlaceSearchService;

    /**
     * 키워드로 장소를 검색합니다.
     *
     * @param query             검색 키워드
     * @param categoryGroupCode 카테고리 그룹 코드
     * @param x                 중심 좌표의 X값 (경도)
     * @param y                 중심 좌표의 Y값 (위도)
     * @param radius            검색 반경 (미터)
     * @param rect              사각형 범위
     * @param page              결과 페이지 번호
     * @param size              한 페이지에 보여질 문서 수
     * @param sort              정렬 옵션 (distance 또는 accuracy)
     * @return 검색 결과
     */
    // @GetMapping("/keyword")
    public ResponseEntity<KakaoPlaceSearchResponse> searchPlacesByKeyword(
            @RequestParam String query,
            @RequestParam(required = false) String categoryGroupCode,
            @RequestParam(required = false) String x,
            @RequestParam(required = false) String y,
            @RequestParam(required = false) Integer radius,
            @RequestParam(required = false) String rect,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort
    ) {
        // log.info("키워드 검색 요청: query={}, x={}, y={}, radius={}", query, x, y, radius);

        KakaoPlaceSearchRequest request = KakaoPlaceSearchRequest.builder()
                .query(query)
                .categoryGroupCode(categoryGroupCode)
                .x(x)
                .y(y)
                .radius(radius)
                .rect(rect)
                .page(page)
                .size(size)
                .sort(sort)
                .build();

        KakaoPlaceSearchResponse response = kakaoPlaceSearchService.searchPlacesByKeyword(request);

        return ResponseEntity.ok(response);
    }
}
