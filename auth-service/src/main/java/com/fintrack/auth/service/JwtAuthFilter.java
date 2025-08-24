package com.fintrack.auth.service;

import com.fintrack.auth.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.fintrack.auth.service.JwtService.ROLE_CLAIM_NAME;

/**
 * JWT authentication filter that intercepts incoming HTTP requests and
 * validates the Bearer token from the Authorization header.
 * <p>
 * If the token is valid, sets the authentication in the {@link SecurityContextHolder}.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String AUTH_HEADER_NAME = "Authorization";

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    /**
     * Filters incoming HTTP requests to perform JWT-based authentication.
     * <p>
     * This method checks the {@code Authorization} header for a Bearer token.
     * If the token is present and valid, it extracts the username and role from the token claims,
     * builds a {@link UsernamePasswordAuthenticationToken}, and sets it into the
     * {@link SecurityContextHolder} to authenticate the request.
     * <p>
     * If the token is missing or invalid, the filter chain continues without authentication.
     *
     * @param request     the HTTP servlet request
     * @param response    the HTTP servlet response
     * @param filterChain the filter chain to pass the request and response to the next filter
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during the filtering process
     */
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String token = Optional.ofNullable(request.getHeader(AUTH_HEADER_NAME))
            .filter(authHeader -> authHeader.startsWith(TOKEN_PREFIX))
            .map(authHeader -> authHeader.substring(TOKEN_PREFIX.length()))
            .orElse("");

        if (token.isEmpty() || !jwtService.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = Jwts.parserBuilder()
            .setSigningKey(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8))
            .build()
            .parseClaimsJws(token)
            .getBody();
        Long userid = Long.parseLong(claims.getSubject());
        String role = claims.get(ROLE_CLAIM_NAME, String.class);
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(ROLE_PREFIX + role)
        );
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userid,
            null,
            authorities
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
