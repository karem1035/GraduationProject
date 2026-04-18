package com.example.Toda.DTO;

import java.time.LocalDate;
import java.util.List;

public record TripDetailsResponse(
        Long id,
        String title,
        String city,
        String meetingPoint,
        String description,
        Integer minGroupSize,
        Integer maxGroupSize,
        String tourDuration,
        LocalDate startDate,
        LocalDate endDate,
        Double pricePerTourist,
        String status,
        String coverImageUrl,
        List<String> categories,
        List<String> inclusions,
        GuideInfo guide
) {
    public record GuideInfo(
            Long id,
            String name,
            String profilePhoto,
            String city,
            Double rating,
            Integer yearsOfExperience
    ) {}
}