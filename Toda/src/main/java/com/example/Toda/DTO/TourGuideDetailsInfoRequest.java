package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record TourGuideDetailsInfoRequest(
        @Schema(description = "Tour delivery format", 
                allowableValues = {"GROUP", "PRIVATE"},
                example = "GROUP")
        String tourType,
        String coveredArea,
        Integer tourDuration
) {
}
