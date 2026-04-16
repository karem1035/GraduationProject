package com.example.Toda.DTO;

public record ChangePasswordRequest(
        String email,
        String otp,
        String newPassword
) {
}
