package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Request body for creating a custom booking request to a tour guide")
public record GuideBookingCreateRequest(
        @Schema(description = "Tour guide user ID to book", example = "5", required = true)
        Long tourGuideId,

        @Schema(description = "Booking title", example = "Custom Cairo Tour")
        String title,

        @Schema(description = "Preferred start date", example = "2026-07-01")
        LocalDate startDate,

        @Schema(description = "Preferred end date", example = "2026-07-03")
        LocalDate endDate,

        @Schema(description = "Description of what you want", example = "I want a tour covering pyramids and museum")
        String description,

        @Schema(description = "Offered price", example = "100.0")
        Double price
) {}