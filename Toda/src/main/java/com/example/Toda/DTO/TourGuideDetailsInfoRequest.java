package com.example.Toda.DTO;

public record TourGuideDetailsInfoRequest(
        String tourType,
        String coveredArea,
        Integer tourDuration
) {
}
