package com.example.Toda.DTO;

public record TripCardResponse(
        Long id,
        String title,
        String city,
        String category,
        String coverImageUrl,
        String duration,
        String status,
        Double pricePerTourist
) {
}
