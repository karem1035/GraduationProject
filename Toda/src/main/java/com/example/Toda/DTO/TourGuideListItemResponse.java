package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Tour guide summary for listing")
public record TourGuideListItemResponse(
        @Schema(description = "User ID")
        Long id,

        @Schema(description = "Guide name")
        String name,

        @Schema(description = "City")
        String city,

        @Schema(description = "Years of experience")
        Integer yearsOfExperience,

        @Schema(description = "Specializations")
        List<String> specialization,

        @Schema(description = "Languages")
        List<String> languages,

        @Schema(description = "Profile photo URL")
        String profilePhoto,

        @Schema(description = "Guide type")
        String guideType,

        @Schema(description = "Tour type")
        String tourType
) {}