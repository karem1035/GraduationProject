package com.example.Toda.Entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Types of landmarks")
public enum LandmarkType {
    @Schema(description = "Monuments and statues")
    MONUMENT,
    @Schema(description = "Museums and galleries")
    MUSEUM,
    @Schema(description = "Natural landmarks (parks, mountains, beaches)")
    NATURAL,
    @Schema(description = "Religious sites (mosques, churches, temples)")
    RELIGIOUS,
    @Schema(description = "Historical sites and ruins")
    HISTORICAL,
    @Schema(description = "Entertainment venues (theme parks, theaters)")
    ENTERTAINMENT,
    @Schema(description = "Restaurants and food destinations")
    RESTAURANT,
    @Schema(description = "Shopping areas (markets, malls)")
    SHOPPING,
    @Schema(description = "Other landmark types")
    OTHER
}