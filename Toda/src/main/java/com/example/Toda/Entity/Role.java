package com.example.Toda.Entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User role types")
public enum Role {
    @Schema(description = "Administrator with full system access")
    ADMIN,
    @Schema(description = "Tourist user who browses and books trips")
    TOURIST,
    @Schema(description = "Tour guide who creates and manages trips")
    TOURGUIDE
}