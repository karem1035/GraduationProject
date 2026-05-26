package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Request object for creating/updating tourist basic information (Step 1)")
public record TouristBasicInfoRequest(
        
        @Schema(description = "Tourist's full name", example = "John Doe", required = true)
        @NotBlank(message = "Name is required")
        String name,
        
        @Schema(description = "Tourist's email address", example = "john.doe@example.com", required = true)
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,
        
        @Schema(description = "Tourist's gender type", example = "MALE", allowableValues = {"MALE", "FEMALE"}, required = true)
        @NotBlank(message = "Type is required")
        String type,
        
        @Schema(description = "Tourist's nationality", example = "USA", required = true)
        @NotBlank(message = "Nationality is required")
        String nationality,
        
        @Schema(description = "Tourist's mother language", example = "English", required = true)
        @NotBlank(message = "Mother language is required")
        String motherLanguage,
        
        @Schema(description = "List of languages the tourist knows", example = "[\"English\", \"Spanish\", \"French\"]")
        List<String> languages,

        @Schema(description = "Tourist's phone number", example = "+201234567890")
        String phone
) {
}
