package com.ssafy.trabuddy.domain.auth.controller;

import com.ssafy.trabuddy.common.jwt.JwtUtil;
import com.ssafy.trabuddy.domain.auth.KakaoLoginJsonData;
import com.ssafy.trabuddy.domain.auth.dto.userInfo.KakaoUserInfoResponse;
import com.ssafy.trabuddy.domain.member.model.dto.LoginMember;
import com.ssafy.trabuddy.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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


    @GetMapping("/v1/auth/kakao/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String uri = String.format("https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code", restApi, redirectUri);
        log.error(uri);
        response.sendRedirect(uri);
    }

    @GetMapping("/v1/auth/kakao/callback")
    public ResponseEntity<LoginMember> kakaoOauth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("enter");
        String code = request.getParameter("code");
        KakaoUserInfoResponse infoResponse = kakaoLoginJsonData.getToken(code);

        long socialId = infoResponse.getId();
        String nickname = infoResponse.getProperties().getNickname();

        memberService.join(infoResponse);

        String token = jwtUtil.generateToken(socialId);
        response.setHeader("Authorization", "Bearer " + token);

        return ResponseEntity.ok(new LoginMember(nickname));
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
