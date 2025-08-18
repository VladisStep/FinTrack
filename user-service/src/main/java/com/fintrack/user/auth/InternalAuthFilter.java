package com.fintrack.user.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class InternalAuthFilter extends OncePerRequestFilter {

    @Value("${s2s.secret}")
    private String s2sSecret;

    @Value("${s2s.header:X-Internal-Auth}")
    private String s2sHeader;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    )  throws ServletException, IOException {
        String value = request.getHeader(s2sHeader);
        if (value != null && value.equals(s2sSecret)) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                "auth-service",
                null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_SERVICE"))
            );
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
