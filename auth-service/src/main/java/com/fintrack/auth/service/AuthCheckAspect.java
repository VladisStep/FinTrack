package com.fintrack.auth.service;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * Aspect that handles authorization logic for methods or classes annotated with {@link AuthCheck}.
 * <p>
 * Checks whether the authenticated user has the required role specified by the annotation.
 * Throws {@link org.springframework.security.access.AccessDeniedException} if access is denied.
 */
@Aspect
@Component
public class AuthCheckAspect  {

    private static final String ROLE_PREFIX = "ROLE_";

    /**
     * Intercepts any method or class annotated with {@link AuthCheck}
     * and verifies that the current user has the required role.
     *
     * @param authCheck the {@code @AuthCheck} annotation instance, from which the required role is extracted
     * @throws AccessDeniedException if the user is not authenticated or lacks the required role
     */
    @Before("@within(authCheck) || @annotation(authCheck)")
    public void checkRole(AuthCheck authCheck) throws AccessDeniedException {
        Authentication auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .filter(Authentication::isAuthenticated)
            .orElseThrow(() -> new AccessDeniedException("Unauthorized"));

        String requiredRole = ROLE_PREFIX + authCheck.value().name();

        auth.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(authority -> !Objects.isNull(authority))
            .filter(requiredRole::equals)
            .findAny()
            .orElseThrow(() -> new AccessDeniedException("Missing role " + requiredRole));
    }
}
