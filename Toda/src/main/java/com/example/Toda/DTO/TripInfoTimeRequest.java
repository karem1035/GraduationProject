package com.example.Toda.DTO;

import java.time.LocalDate;

public record TripInfoTimeRequest(
        LocalDate startDate,
        LocalDate endDate,
        String description,
        Integer minGroupSize,
        Integer maxGroupSize,
        String tourDuration
) {
}
