package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Full landmark details response")
public record LandmarkResponse(

        @Schema(description = "Landmark ID", example = "1")
        Long id,

        @Schema(description = "Name of the landmark", example = "Pyramids of Giza")
        String name,

        @Schema(description = "Description of the landmark", example = "Ancient Egyptian pyramid complex")
        String description,

        @Schema(description = "City where the landmark is located", example = "Giza")
        String city,

        @Schema(description = "Full address of the landmark", example = "Al Haram, Nazlet El-Semman, Giza Governorate")
        String address,

        @Schema(description = "Type of the landmark", example = "MONUMENT")
        String type,

        @Schema(description = "URL of the landmark image")
        String imageUrl
) {}