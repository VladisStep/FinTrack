package com.fintrack.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest (

    @NotNull
    Long id,

    @NotBlank
    String username,

    @NotBlank
    @Email(message = "Invalid email format")
    String email,

    @NotBlank
    String role
) {}
