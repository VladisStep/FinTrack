package com.fintrack.user.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String ROLE_CLAIM = "role";
    private static final String ROLE_PREFIX = "ROLE_";

    private final JwtProperties jwtProperties;

    public JwtAuthFilter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String token = Optional.ofNullable(request.getHeader("Authorization"))
            .filter(h -> h.startsWith(TOKEN_PREFIX))
            .map(h -> h.substring(TOKEN_PREFIX.length()))
            .orElse(null);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

            String username = claims.getSubject();
            String role = claims.get(ROLE_CLAIM, String.class);

            var auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority(ROLE_PREFIX + role))
            );
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception ignore) {
            // invalid/expired token -> simply not authenticating
            // todo ???
        }

        filterChain.doFilter(request, response);
    }
}
