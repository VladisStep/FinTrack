package com.fintrack.auth.dto;

public record RegisterRequest(
    String username,
    String password
) {}

