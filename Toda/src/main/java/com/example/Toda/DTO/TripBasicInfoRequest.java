package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Request body for creating a trip with basic information (Step 1)")
public record TripBasicInfoRequest(
        @Schema(description = "Trip title", example = "Pyramids Adventure", required = true)
        String title,

        @Schema(description = "Trip categories", example = "[\"historical\", \"adventure\"]", required = true)
        List<String> category,

        @Schema(description = "City where the trip takes place", example = "Giza", required = true)
        String city,

        @Schema(description = "Meeting point for the trip", example = "Giza Plateau entrance", required = true)
        String meetingPoint,

        @Schema(description = "Optional list of landmark IDs to attach to the trip", example = "[1, 2, 5]")
        List<Long> landmarkIds
) {
}
