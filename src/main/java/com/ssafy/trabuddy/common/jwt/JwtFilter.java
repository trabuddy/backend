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

    private final String[] excludePath = {"/api/swagger-ui/**",
            "/api/swagger-ui.html",
            "/api/v3/api-docs/**",
            "/api/swagger-resources/**",
            "/api/webjars/**",
            "/api/swagger-ui/index.html",
            "/api/api/v1/auth/kakao/login",
            "/api/v1/auth/kakao/callback", "/api/v1/auth/check", "/api/v1/attractions"};
    private final String[] planPaths = {"/api/v1/plans"};
    private final String[] getExcludePath = {"/api/v1/plans"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
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

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    Collections.emptyList()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authentication set in SecurityContext: {}", authentication);

            response.setHeader("Authorization", "Bearer " + token);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error processing JWT token", e);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }

    private boolean isPlanRequest(String requestURI) {
        return Arrays.stream(planPaths).anyMatch(requestURI::startsWith);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if(request.getMethod().equalsIgnoreCase("GET") &&
                Arrays.stream(getExcludePath).anyMatch(request.getRequestURI()::startsWith))
            return true;

        return Arrays.stream(excludePath).anyMatch(request.getRequestURI()::startsWith);
    }
}
