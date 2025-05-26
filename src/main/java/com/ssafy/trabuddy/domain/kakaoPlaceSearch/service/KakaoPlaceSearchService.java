package com.ssafy.trabuddy.domain.kakaoPlaceSearch.service;

import com.ssafy.trabuddy.domain.kakaoPlaceSearch.model.dto.KakaoPlaceSearchRequest;
import com.ssafy.trabuddy.domain.kakaoPlaceSearch.model.dto.KakaoPlaceSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoPlaceSearchService {

    private final RestTemplate restTemplate;

    @Value("${kakao.local.restApi}")
    private String kakaoLocalApiKey;

    private static final String KAKAO_LOCAL_SEARCH_KEYWORD_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    /**
     * 키워드로 장소를 검색합니다.
     *
     * @param request 검색 요청 정보
     * @return 검색 결과
     */
    public KakaoPlaceSearchResponse searchPlacesByKeyword(KakaoPlaceSearchRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoLocalApiKey);

        URI uri = buildSearchUri(request);

        ResponseEntity<KakaoPlaceSearchResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                KakaoPlaceSearchResponse.class
        );

        // log.info("Kakao Local API 검색 결과: {}", response.getBody());

        return response.getBody();
    }

    /**
     * 키워드로 장소를 검색합니다 (간단한 버전).
     *
     * @param query 검색 키워드
     * @return 검색 결과
     */
    public KakaoPlaceSearchResponse searchPlacesByKeyword(String query) {
        KakaoPlaceSearchRequest request = KakaoPlaceSearchRequest.builder()
                .query(query)
                .size(1) // 첫 번째 결과만 가져오기
                .build();

        return searchPlacesByKeyword(request);
    }

    /**
     * 검색 요청 정보를 기반으로 URI를 생성합니다.
     *
     * @param request 검색 요청 정보
     * @return 생성된 URI
     */
    private URI buildSearchUri(KakaoPlaceSearchRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_KEYWORD_URL)
                .queryParam("query", request.getQuery());

        if (request.getCategoryGroupCode() != null) {
            builder.queryParam("category_group_code", request.getCategoryGroupCode());
        }

        if (request.getX() != null && request.getY() != null) {
            builder.queryParam("x", request.getX())
                    .queryParam("y", request.getY());

            if (request.getRadius() != null) {
                builder.queryParam("radius", request.getRadius());
            }
        }

        if (request.getRect() != null) {
            builder.queryParam("rect", request.getRect());
        }

        if (request.getPage() != null) {
            builder.queryParam("page", request.getPage());
        }

        if (request.getSize() != null) {
            builder.queryParam("size", request.getSize());
        }

        if (request.getSort() != null) {
            builder.queryParam("sort", request.getSort());
        }

        return builder.build(true).toUri();
    }
}
