package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Request body for creating a static trip")
public record StaticTripCreateRequest(
        @Schema(description = "Trip title", example = "Amazing Cairo Tour", required = true)
        String title,

        @Schema(description = "Trip description", example = "Explore the wonders of Cairo")
        String description,

        @Schema(description = "City", example = "Cairo")
        String city,

        @Schema(description = "Meeting point", example = "Tahrir Square")
        String meetingPoint,

        @Schema(description = "Start date", example = "2026-07-01")
        LocalDate startDate,

        @Schema(description = "End date", example = "2026-07-03")
        LocalDate endDate,

        @Schema(description = "Price per person", example = "50.0")
        Double price,

        @Schema(description = "Trip duration", example = "3 hours")
        String duration,

        @Schema(description = "Maximum group size", example = "15")
        Integer groupSize,

        @Schema(description = "List of categories", example = "[\"Historical\", \"Cultural\"]")
        List<String> categories,

        @Schema(description = "List of inclusions", example = "[\"Transport\", \"Guide\"]")
        List<String> inclusions
) {}