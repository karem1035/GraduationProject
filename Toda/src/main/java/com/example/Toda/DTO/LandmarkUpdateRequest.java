package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body for updating an existing landmark")
public record LandmarkUpdateRequest(

        @Schema(description = "Name of the landmark", example = "Pyramids of Giza")
        String name,

        @Schema(description = "Description of the landmark", example = "Ancient Egyptian pyramid complex")
        String description,

        @Schema(description = "City where the landmark is located", example = "Giza")
        String city,

        @Schema(description = "Full address of the landmark", example = "Al Haram, Nazlet El-Semman, Giza Governorate")
        String address,

        @Schema(description = "Type of the landmark", example = "MONUMENT",
                allowableValues = {"MONUMENT", "MUSEUM", "NATURAL", "RELIGIOUS", "HISTORICAL", "ENTERTAINMENT", "RESTAURANT", "SHOPPING", "OTHER"})
        String type
) {}