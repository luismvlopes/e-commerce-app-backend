package com.youtube.e_commerce_backend.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;


@Configuration
public class WebSecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


    return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .addFilterBefore(jwtRequestFilter, AuthorizationFilter.class)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/product", "/auth/register", "/auth/login",
                            "/auth/forgot-password", "/auth/reset-password", "/auth/verify", "/error").permitAll()
                    .anyRequest().authenticated())
            .build();

    }
}
