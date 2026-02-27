package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Response object for tourist travel information (Step 2)")
public record TouristTravelInfoResponse(
        
        @Schema(description = "Tourist's unique ID")
        Long id,
        
        @Schema(description = "Travel start date")
        LocalDate travelDateFrom,
        
        @Schema(description = "Travel end date")
        LocalDate travelDateTo,
        
        @Schema(description = "Destination city")
        String destinationCity,
        
        @Schema(description = "Type of trip")
        String tripType,
        
        @Schema(description = "Number of travelers")
        Integer numberOfTravelers
) {
}