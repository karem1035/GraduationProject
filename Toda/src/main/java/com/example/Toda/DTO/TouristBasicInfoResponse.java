package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response object for tourist basic information (Step 1)")
public record TouristBasicInfoResponse(
        
        @Schema(description = "Tourist's unique ID")
        Long id,
        
        @Schema(description = "Tourist's full name")
        String name,
        
        @Schema(description = "Tourist's email address")
        String email,
        
        @Schema(description = "Tourist's gender type")
        String type,
        
        @Schema(description = "Tourist's nationality")
        String nationality,
        
        @Schema(description = "Tourist's mother language")
        String motherLanguage,
        
        @Schema(description = "List of languages the tourist knows")
        List<String> languages,

        @Schema(description = "Tourist's phone number")
        String phone
) {
}
