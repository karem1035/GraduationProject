package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response object for tourist special needs and preferences (Step 4)")
public record TouristPreferencesResponse(
        
        @Schema(description = "Tourist's unique ID")
        Long id,
        
        @Schema(description = "Special needs category")
        String specialNeeds,
        
        @Schema(description = "List of travel preferences")
        List<String> travelPreferences,
        
        @Schema(description = "Food preference")
        String foodPreference,
        
        @Schema(description = "Food allergies or dietary restrictions")
        String foodAllergies,
        
        @Schema(description = "Additional notes or special requests")
        String notes
) {
}