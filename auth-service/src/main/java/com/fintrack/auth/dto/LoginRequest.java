package com.fintrack.auth.dto;

public record LoginRequest(
    String username,
    String password
) {}
