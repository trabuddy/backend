package com.ssafy.trabuddy.domain.auth;

import com.ssafy.trabuddy.domain.auth.dto.userInfo.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoLoginJsonData {
    private final RestTemplate restTemplate;

    @Value("${kakao.login.restApi}")
    private String restApi;
    @Value("${kakao.login.redirectUri}")
    private String redirectUri;

    public KakaoUserInfoResponse getToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        String url = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("client_id", restApi);
        body.add("redirect_uri", redirectUri);
        body.add("response_type", "code");
        body.add("grant_type", "authorization_code");
        body.add("code", code);

        KakaoJsonDataResponse response = restTemplate.postForObject(url, new HttpEntity<>(body, headers), KakaoJsonDataResponse.class);

        return getUserInfo(response);
    }

    private KakaoUserInfoResponse getUserInfo(KakaoJsonDataResponse response) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.add("Authorization", "Bearer " + response.getAccess_token());

        KakaoUserInfoResponse info = restTemplate.postForEntity(url, new HttpEntity<>(headers), KakaoUserInfoResponse.class)
                .getBody();

        return info;
    }
}
