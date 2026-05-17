package com.example.Toda.Entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Trip lifecycle status")
public enum TripStatus {
    @Schema(description = "Trip is newly created, not yet published")
    NEW,
    @Schema(description = "Trip is published and visible to tourists")
    UPCOMING,
    @Schema(description = "Trip has been completed")
    COMPLETED,
    @Schema(description = "Trip has been cancelled")
    CANCELLED
}