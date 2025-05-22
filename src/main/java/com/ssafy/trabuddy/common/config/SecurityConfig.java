package com.ssafy.trabuddy.common.config;

import com.ssafy.trabuddy.common.jwt.JwtFilter;
import com.ssafy.trabuddy.common.jwt.JwtUtil;
import com.ssafy.trabuddy.domain.member.mapper.MemberMapper;
import com.ssafy.trabuddy.domain.member.model.entity.MemberEntity;
import com.ssafy.trabuddy.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(HttpBasicConfigurer::disable)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(config ->
                        config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(jwtUtil, userDetailService()), UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailService())
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers("/api/v1/plans/**").authenticated()
                            .anyRequest().permitAll();
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
