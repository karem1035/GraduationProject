package com.example.Toda.DTO;

import java.util.List;

public record TourGuideProfessionalInfoRequest(
        String guideType,
         String licensedNumber,
         Integer yearsOfExperience,
        List<String> specialization

) {

}
