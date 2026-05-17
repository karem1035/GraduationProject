package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body for changing password via OTP verification")
public record ChangePasswordRequest(
        @Schema(description = "User email address", example = "john.doe@example.com", required = true)
        String email,

        @Schema(description = "OTP code received via email", example = "123456", required = true)
        String otp,

        @Schema(description = "New password", example = "NewSecurePass456!", required = true)
        String newPassword
) {
}