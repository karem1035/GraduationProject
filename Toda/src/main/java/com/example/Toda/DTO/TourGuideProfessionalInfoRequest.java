package com.example.Toda.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TourGuideProfessionalInfoRequest(
        @Schema(description = "Guide certification level", 
                allowableValues = {"LICENSED_GUIDE", "LOCAL_GUIDE"},
                example = "LICENSED_GUIDE")
        String guideType,
         String licensedNumber,
         Integer yearsOfExperience,
        List<String> specialization

) {

}
