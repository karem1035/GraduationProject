package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(description = "Request object for creating/updating tourist special needs and preferences (Step 4)")
public record TouristPreferencesRequest(
        
        @Schema(description = "Special needs category", 
                example = "MOBILITY_ASSISTANCE",
                allowableValues = {"MOBILITY_ASSISTANCE", "VISUAL_HEARING_SUPPORT", "MEDICAL_CONSIDERATIONS"},
                required = true)
        @NotBlank(message = "Special needs is required")
        String specialNeeds,
        
        @Schema(description = "List of travel preferences", 
                example = "[\"Prefer private tours\", \"Flexible schedule\"]",
                required = true)
        List<String> travelPreferences,
        
        @Schema(description = "Food preference", 
                example = "VEGETARIAN",
                allowableValues = {"VEGETARIAN", "VEGAN", "HALAL"},
                required = true)
        @NotBlank(message = "Food preference is required")
        String foodPreference,
        
        @Schema(description = "Food allergies or dietary restrictions", example = "Peanuts, Shellfish")
        String foodAllergies,
        
        @Schema(description = "Additional notes or special requests", example = "Need wheelchair accessibility at all locations")
        String notes
) {
}