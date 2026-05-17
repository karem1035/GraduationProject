package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Request body for creating a booking request on a trip")
public record BookingCreateRequest(
        @Schema(description = "ID of the trip to book", example = "1", required = true)
        Long tripId,

        @Schema(description = "Booking category", example = "standard", required = true)
        String category,

        @Schema(description = "Preferred date and time for the trip", example = "2026-06-15T10:00:00", required = true)
        LocalDateTime date,

        @Schema(description = "Number of tourists", example = "2", required = true)
        Integer touristCount,

        @Schema(description = "Additional notes or special requests", example = "We have a wheelchair user in our group")
        String notes
) {}