package com.fintrack.user.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] SWAGGER = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html"
    };

    private final JwtAuthFilter jwtAuthFilter;
    private final InternalAuthFilter internalAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, InternalAuthFilter internalAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.internalAuthFilter = internalAuthFilter;
    }

    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(SWAGGER).permitAll()
                // an internal call to create a user is a service only
                .requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("SERVICE")
                // the rest is according to JWT
                .anyRequest().authenticated()
            )
            .addFilterBefore(internalAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
