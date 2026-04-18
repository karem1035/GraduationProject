package com.example.Toda.DTO;

public record TouristBookingResponse(
        Long requestId,
        String status,
        String category,
        String date,
        Integer touristCount,
        // Trip info
        Long tripId,
        String tripTitle,
        String tripCoverImage,
        String tripCity,
        String tripDuration,
        Double pricePerTourist,
        // Guide info
        Long guideId,
        String guideName,
        String guidePhoto
) {}