package com.example.Toda.DTO;

import java.time.LocalDateTime;

public record BookingCreateRequest(
        Long tripId,
        String category,
        LocalDateTime date,
        Integer touristCount,
        String notes
) {}