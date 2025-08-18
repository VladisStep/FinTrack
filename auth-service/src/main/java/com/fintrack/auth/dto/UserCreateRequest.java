package com.fintrack.auth.dto;


public record UserCreateRequest (
    Long id,
    String username,
    String email,
    String role
) {}
