package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Request object for creating/updating tourist travel interests (Step 3)")
public record TouristInterestsRequest(
        
        @Schema(description = "List of travel interests", 
                example = "[\"Historical Sites\", \"Adventure Tours\", \"Local Cuisine\", \"Museums\"]",
                required = true)
        @NotNull(message = "Travel interests are required")
        @NotEmpty(message = "At least one travel interest is required")
        List<String> travelInterests
) {
}