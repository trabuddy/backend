package com.ssafy.trabuddy.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(config ->
                        config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers("/**").permitAll();
//                    authorizeRequests.requestMatchers("/api/v1/auth/kakao/callback").permitAll();
//                    authorizeRequests.requestMatchers("/api/v1/auth/kakao/login").permitAll();
//                    authorizeRequests.requestMatchers("/api/v1/auth/test").hasAnyRole("USER");
                });

        return http.build();
    }

}
