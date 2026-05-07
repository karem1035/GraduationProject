package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for user registration")
public record RegisterRequest(
        @Schema(description = "Username", example = "johndoe", required = true)
        @NotBlank(message = "Username is required")
        String username,
        
        @Schema(description = "Email address", example = "john.doe@example.com", required = true)
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,
        
        @Schema(description = "Password", example = "SecurePass123!", required = true)
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,
        
        @Schema(description = "User role", 
                allowableValues = {"ADMIN", "TOURIST", "TOURGUIDE"},
                example = "TOURIST",
                required = true)
        @NotBlank(message = "Role is required")
        String role
) {
}
