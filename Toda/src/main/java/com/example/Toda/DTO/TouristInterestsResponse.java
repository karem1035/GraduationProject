package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response object for tourist travel interests (Step 3)")
public record TouristInterestsResponse(
        
        @Schema(description = "Tourist's unique ID")
        Long id,
        
        @Schema(description = "List of travel interests")
        List<String> travelInterests
) {
}