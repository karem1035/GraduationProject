package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Response for a guide booking request")
public record GuideBookingResponse(
        @Schema(description = "Booking request ID")
        Long id,

        @Schema(description = "Booking title")
        String title,

        @Schema(description = "Start date")
        LocalDate startDate,

        @Schema(description = "End date")
        LocalDate endDate,

        @Schema(description = "Description")
        String description,

        @Schema(description = "Offered price")
        Double price,

        @Schema(description = "Booking status", allowableValues = {"PENDING", "ACCEPTED", "REJECTED"})
        String status,

        @Schema(description = "Creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Tourist info")
        UserInfo tourist,

        @Schema(description = "Tour guide info")
        UserInfo tourGuide
) {
    public record UserInfo(
            @Schema(description = "User ID")
            Long id,

            @Schema(description = "Username")
            String username,

            @Schema(description = "Profile photo URL")
            String profilePhoto
    ) {}
}