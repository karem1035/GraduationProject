package com.example.Toda.DTO;

public record RegisterRequest(
        String username,
        String email,
        String password,
        String role
) {
}
