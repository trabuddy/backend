package com.ssafy.trabuddy.domain.auth.controller;

import com.mysema.commons.lang.URLEncoder;
import com.ssafy.trabuddy.common.jwt.JwtUtil;
import com.ssafy.trabuddy.domain.auth.KakaoLoginJsonData;
import com.ssafy.trabuddy.domain.auth.dto.userInfo.KakaoUserInfoResponse;
import com.ssafy.trabuddy.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final KakaoLoginJsonData kakaoLoginJsonData;
    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Value("${kakao.login.restApi}")
    private String restApi;

    @Value("${kakao.login.redirectUri}")
    private String redirectUri;

    @Value("${frontend.url}")
    private String frontendUrl;

    @GetMapping("/v1/auth/kakao/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String uri = String.format("https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code", restApi, redirectUri);
        log.error(uri);
        response.sendRedirect(uri);
    }

    @GetMapping("/v1/auth/kakao/callback")
    public void kakaoOauth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("enter");
        String code = request.getParameter("code");
        KakaoUserInfoResponse infoResponse = kakaoLoginJsonData.getToken(code);

        long socialId = infoResponse.getId();
        String nickname = infoResponse.getProperties().getNickname();

        memberService.join(infoResponse);

        String token = jwtUtil.generateToken(socialId);

        // HTTP-only 쿠키에 토큰 저장
        ResponseCookie cookie = ResponseCookie.from("auth_token", token)
                .sameSite("none")
                .secure(true)
                .path("/")
                .maxAge(3600)
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
        // 닉네임은 쿼리 파라미터로 전달 (닉네임은 민감한 정보가 아니므로)
        String redirectUrl = String.format("%s?nickname=%s", frontendUrl, URLEncoder.encodeURL(nickname));
        log.info(redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/v1/auth/test")
    public String test() {
        log.error("enter Test");
        return "test";
    }

    @GetMapping("/v1/auth/check")
    public String check() {
        return "redirectTest";
    }

    @GetMapping("/v1/auth/check2")
    public String check2() {
        return "redirectTest2";
    }
}
