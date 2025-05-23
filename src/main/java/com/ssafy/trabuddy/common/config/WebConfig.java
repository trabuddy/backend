package com.ssafy.trabuddy.common.config;

import com.ssafy.trabuddy.domain.member.service.MemberService;
import com.ssafy.trabuddy.domain.plan.interceptor.CheckAuthorizationForPlanInterceptor;
import com.ssafy.trabuddy.domain.plan.service.PlanService;
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


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckAuthorizationForPlanInterceptor(planService))
                .addPathPatterns("/v1/plans/**");
    }
}
