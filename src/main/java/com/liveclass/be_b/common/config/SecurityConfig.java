package com.liveclass.be_b.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liveclass.be_b.security.jwt.JwtAuthenticationFilter;
import com.liveclass.be_b.security.jwt.JwtProvider;
import com.liveclass.be_b.security.handler.CustomAccessDeniedHandler;
import com.liveclass.be_b.security.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider, objectMapper);
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler(objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                        .accessDeniedHandler(customAccessDeniedHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        // Swagger 관련 경로 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api-docs/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/creator/login",
                                "/admin/login"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/sales").permitAll()
                        .requestMatchers(HttpMethod.POST, "/sales/*/cancellations").permitAll()
                        .requestMatchers(HttpMethod.GET, "/sales").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.POST, "/creator/settlements").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET, "/creator/settlements").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET,"/creator/settlements/monthly").hasRole("CREATOR")
                        .requestMatchers(HttpMethod.GET, "/admin/settlements").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/settlement-management").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/settlement-management/*/confirm").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/admin/settlement-management/*/pay").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
