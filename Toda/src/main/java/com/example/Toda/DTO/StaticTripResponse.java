package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Full details of a static trip")
public record StaticTripResponse(
        @Schema(description = "Trip ID")
        Long id,

        @Schema(description = "Trip title")
        String title,

        @Schema(description = "Trip description")
        String description,

        @Schema(description = "City")
        String city,

        @Schema(description = "Meeting point")
        String meetingPoint,

        @Schema(description = "Start date")
        LocalDate startDate,

        @Schema(description = "End date")
        LocalDate endDate,

        @Schema(description = "Price per person")
        Double price,

        @Schema(description = "Trip duration")
        String duration,

        @Schema(description = "Maximum group size")
        Integer groupSize,

        @Schema(description = "Cover image URL")
        String imageUrl,

        @Schema(description = "Categories")
        List<String> categories,

        @Schema(description = "Inclusions")
        List<String> inclusions,

        @Schema(description = "Creator info")
        CreatorInfo creator
) {
    public record CreatorInfo(
            @Schema(description = "Creator user ID")
            Long id,

            @Schema(description = "Creator username")
            String username,

            @Schema(description = "Creator profile photo")
            String profilePhoto
    ) {}
}