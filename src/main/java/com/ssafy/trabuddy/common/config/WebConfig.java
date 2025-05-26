package com.ssafy.trabuddy.common.config;

import com.ssafy.trabuddy.domain.invite.interceptor.PlanOwnerInterceptor;
import com.ssafy.trabuddy.domain.member.service.MemberService;
import com.ssafy.trabuddy.domain.plan.interceptor.CheckAuthorizationForPlanInterceptor;
import com.ssafy.trabuddy.domain.plan.service.PlanService;
import com.ssafy.trabuddy.domain.point.interceptor.CheckAuthorizationInterceptor;
import com.ssafy.trabuddy.domain.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private final MemberService memberService;
    private final PlanService planService;
    private final PointService pointService;
    private final PlanOwnerInterceptor planOwnerInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAuthorizationForPlanInterceptor(planService))
                .addPathPatterns("/api/v1/plans/**");

        registry.addInterceptor(new CheckAuthorizationInterceptor(pointService))
                .addPathPatterns("/api/v1/plans/*/points/**");

        // 초대 링크 생성 시 주최자 권한 체크
        registry.addInterceptor(planOwnerInterceptor)
                .addPathPatterns("/api/v1/plans/*/invite");

        // 참여자 관리 시 주최자 권한 체크 (PUT, DELETE만)
        registry.addInterceptor(planOwnerInterceptor)
                .addPathPatterns("/api/v1/plans/*/participants/*");
    }
}
