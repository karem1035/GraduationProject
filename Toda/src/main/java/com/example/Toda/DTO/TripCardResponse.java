package com.example.Toda.DTO;

import java.time.LocalDate;

public record TripCardResponse(
        Long id,
        String title,
        String city,
        String category,
        String coverImageUrl,
        String duration,
        String status,
        Double pricePerTourist,
        LocalDate startDate,
        LocalDate endDate
) {
}
