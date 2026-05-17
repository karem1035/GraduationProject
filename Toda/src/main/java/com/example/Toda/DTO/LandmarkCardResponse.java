package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Lightweight landmark card for lists and trip details")
public record LandmarkCardResponse(

        @Schema(description = "Landmark ID", example = "1")
        Long id,

        @Schema(description = "Name of the landmark", example = "Pyramids of Giza")
        String name,

        @Schema(description = "City where the landmark is located", example = "Giza")
        String city,

        @Schema(description = "Type of the landmark", example = "MONUMENT")
        String type,

        @Schema(description = "URL of the landmark image")
        String imageUrl
) {}