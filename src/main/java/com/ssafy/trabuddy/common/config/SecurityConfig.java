package com.ssafy.trabuddy.common.config;

import com.ssafy.trabuddy.common.jwt.JwtFilter;
import com.ssafy.trabuddy.common.jwt.JwtUtil;
import com.ssafy.trabuddy.domain.member.mapper.MemberMapper;
import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final MemberRepository userRepository;
    private final JwtUtil jwtUtil;

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtil, userDetailService());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(config ->
                        config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
//                    authorizeRequests.requestMatchers(HttpMethod.GET, "/api/v1/plans/**").permitAll();
                    authorizeRequests.requestMatchers(
                            "/api/swagger-ui/**",
                            "/api/swagger-ui.html",
                            "/api/v3/api-docs/**",
                            "/api/swagger-resources/**",
                            "/api/webjars/**",
                            "/api/swagger-ui/index.html",
                            "/api/v1/auth/kakao/callback",
                            "/api/v1/auth/kakao/login",
                            "/api/v1/auth/check",
                            "/api/v1/auth/test",
                            "/api/v1/attractions"
                    ).permitAll();
                    // 인증이 필요한 API들
                    authorizeRequests.requestMatchers(
                            "/api/v1/users/**",
                            "/api/v1/plans/**"
                    ).authenticated();
                    // 나머지 요청들은 일단 허용하되, 인증된 요청은 처리
                    authorizeRequests.anyRequest().permitAll();
                });

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailService() {
        return socialId -> {
            MemberEntity user = userRepository.findBySocialIdAndUnregisteredAtIsNull(Long.parseLong(socialId)).orElseThrow(()
                    -> new UsernameNotFoundException(socialId));
            return MemberMapper.INSTANCE.entityToDto(user);
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
