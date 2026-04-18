package com.example.Toda.DTO;

public record BookingRequestResponse(
        Long requestId,
        String touristName,
        String touristImage,
        String category,
        String date,
        Integer touristCount,
        String status,
        // Trip info
        Long tripId,
        String tripTitle,
        String tripCoverImage
) {}