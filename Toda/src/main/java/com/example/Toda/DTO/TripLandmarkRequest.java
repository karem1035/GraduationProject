package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Request body for attaching landmarks to a trip")
public record TripLandmarkRequest(

        @Schema(description = "List of landmark IDs to attach to the trip", example = "[1, 2, 3]", required = true)
        List<Long> landmarkIds
) {}