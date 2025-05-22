package com.ssafy.trabuddy.common.jwt;

import com.ssafy.trabuddy.domain.member.model.dto.LoggedInMember;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailService;
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final String[] excludePath = {"/api/v1/auth/kakao/login", "/api/v1/auth/kakao/callback", "/api/v1/auth/check", "/api/v1/attractions"};
    private final String[] planPaths = {"/api/v1/plans"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            log.info("No Authorization header found");
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
        String socialId = String.valueOf(jwtUtil.parse(token, response).get("socialId"));
        log.info("Parsed socialId from token: {}", socialId);

        LoggedInMember user = (LoggedInMember) userDetailService.loadUserByUsername(socialId);
        log.info("Loaded user: {}", user);

        // plan 관련 요청인 경우에만 ROLE_MEMBER 부여
        if (isPlanRequest(request.getRequestURI())) {
            log.info("Plan request detected, setting ROLE_MEMBER");
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEMBER"))
                    ));
        } else {
            log.info("Non-plan request, setting authentication without role");
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Collections.emptyList()
                    ));
        }

        response.setHeader("Authorization", "Bearer " + token);
        filterChain.doFilter(request, response);
    }

    private boolean isPlanRequest(String requestURI) {
        return Arrays.stream(planPaths).anyMatch(requestURI::startsWith);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(excludePath).anyMatch(request.getRequestURI()::startsWith);
    }
}
