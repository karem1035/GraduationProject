package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body for updating user profile information")
public record updateProfileRequest(
        @Schema(description = "User first name", example = "John")
        String firstName,

        @Schema(description = "User last name", example = "Doe")
        String lastName,

        @Schema(description = "User email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "User phone number", example = "+201234567890")
        String phone
) {
}