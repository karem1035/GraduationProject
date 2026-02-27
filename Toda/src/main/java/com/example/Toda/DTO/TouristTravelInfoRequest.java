package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Schema(description = "Request object for creating/updating tourist travel information (Step 2)")
public record TouristTravelInfoRequest(
        
        @Schema(description = "Travel start date", example = "2025-06-15", required = true)
        @NotNull(message = "Travel date from is required")
        LocalDate travelDateFrom,
        
        @Schema(description = "Travel end date", example = "2025-06-20", required = true)
        @NotNull(message = "Travel date to is required")
        LocalDate travelDateTo,
        
        @Schema(description = "Destination city", example = "Cairo", required = true)
        @NotBlank(message = "Destination city is required")
        String destinationCity,
        
        @Schema(description = "Type of trip", example = "Private Tour", required = true)
        @NotBlank(message = "Trip type is required")
        String tripType,
        
        @Schema(description = "Number of travelers", example = "4", required = true)
        @NotNull(message = "Number of travelers is required")
        @Positive(message = "Number of travelers must be positive")
        Integer numberOfTravelers
) {
}