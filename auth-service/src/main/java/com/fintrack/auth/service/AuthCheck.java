package com.fintrack.auth.service;

import com.fintrack.auth.model.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for declarative role-based access control.
 * <p>
 * Can be applied to controller methods or entire classes to restrict access
 * based on the user's role extracted from the security context (e.g. JWT token).
 * <p>
 * Used in conjunction with {@link com.fintrack.auth.service.AuthCheckAspect}.
 *
 * <pre>
 * Example usage:
 * {@code
 * @AuthCheck(Role.ADMIN)
 * @GetMapping("/admin")
 * public String adminEndpoint() {
 *     return "Only admins can access this";
 * }
 * }
 * </pre>
 *
 * @see com.fintrack.auth.service.AuthCheckAspect
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * Required user role to access the annotated method or class.
     *
     * @return role required for access
     */
    Role value();
}
