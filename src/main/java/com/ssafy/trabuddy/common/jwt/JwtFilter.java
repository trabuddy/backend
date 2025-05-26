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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

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
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String requestId = requestURI + "-" + System.currentTimeMillis();
        
        log.info("JWT Filter [{}] - Processing request: {} {}", requestId, method, requestURI);
        
        // 이미 인증되어 있는지 확인
        var existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (existingAuth != null && existingAuth.isAuthenticated() && existingAuth.getPrincipal() instanceof LoggedInMember) {
            log.info("JWT Filter [{}] - Already authenticated, skipping: {}", requestId, existingAuth.getPrincipal());
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            String header = request.getHeader("Authorization");
            log.info("JWT Filter [{}] - Authorization header: {}", requestId, header != null ? "Bearer ***" : "null");
            
            if (header == null || !header.startsWith("Bearer ")) {
                log.warn("JWT Filter [{}] - No valid Authorization header found for {} {}", requestId, method, requestURI);
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.replace("Bearer ", "");
            log.info("JWT Filter [{}] - Token extracted successfully", requestId);
            
            String socialId = String.valueOf(jwtUtil.parse(token, response).get("socialId"));
            log.info("JWT Filter [{}] - Parsed socialId from token: {}", requestId, socialId);

            LoggedInMember user = (LoggedInMember) userDetailService.loadUserByUsername(socialId);
            log.info("JWT Filter [{}] - Loaded user: {}", requestId, user);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    Collections.emptyList()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("JWT Filter [{}] - Authentication set in SecurityContext: {}", requestId, authentication);

            response.setHeader("Authorization", "Bearer " + token);
            filterChain.doFilter(request, response);
            
            log.info("JWT Filter [{}] - Request processing completed", requestId);
        } catch (Exception e) {
            log.error("JWT Filter [{}] - Error processing JWT token for {} {}: {}", requestId, method, requestURI, e.getMessage(), e);
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }

    private boolean isPlanRequest(String requestURI) {
        return Arrays.stream(planPaths).anyMatch(requestURI::startsWith);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String requestId = requestURI + "-" + System.currentTimeMillis();
        
        log.info("JWT Filter [{}] - shouldNotFilter check for: {} {}", requestId, method, requestURI);
        
        // 사용자 관련 API는 항상 필터를 통과해야 함 (제외하지 않음)
        if (requestURI.startsWith("/api/v1/users/")) {
            log.info("JWT Filter [{}] - User API detected, will process through filter: {}", requestId, requestURI);
            return false;
        }
        
        if(request.getMethod().equalsIgnoreCase("GET") &&
                Arrays.stream(getExcludePath).anyMatch(request.getRequestURI()::startsWith)) {
            log.info("JWT Filter [{}] - GET request excluded: {}", requestId, requestURI);
            return true;
        }

        boolean isExcluded = Arrays.stream(excludePath).anyMatch(request.getRequestURI()::startsWith);
        if (isExcluded) {
            log.info("JWT Filter [{}] - Request excluded by excludePath: {}", requestId, requestURI);
        } else {
            log.info("JWT Filter [{}] - Request will be processed: {}", requestId, requestURI);
        }
        
        return isExcluded;
    }
}
