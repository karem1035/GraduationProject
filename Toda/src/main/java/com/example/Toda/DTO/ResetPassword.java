package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body for resetting password")
public record ResetPassword(
        @Schema(description = "User email address", example = "john.doe@example.com", required = true)
        String email,

        @Schema(description = "New password", example = "NewSecurePass456!", required = true)
        String newPassword
) {
}