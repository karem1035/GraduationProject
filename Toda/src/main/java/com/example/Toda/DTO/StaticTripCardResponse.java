package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Card summary of a static trip for list views")
public record StaticTripCardResponse(
        @Schema(description = "Trip ID")
        Long id,

        @Schema(description = "Trip title")
        String title,

        @Schema(description = "City")
        String city,

        @Schema(description = "Start date")
        LocalDate startDate,

        @Schema(description = "End date")
        LocalDate endDate,

        @Schema(description = "Price per person")
        Double price,

        @Schema(description = "Trip duration")
        String duration,

        @Schema(description = "Cover image URL")
        String imageUrl,

        @Schema(description = "Creator username")
        String creatorName
) {}