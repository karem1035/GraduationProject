package com.example.Toda.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourGuideRequest {
    private String name;
    private String email;
    private String type; // MALE or FEMALE
    private String phone;
    private String city;
    private String guideType; // LICENSED_GUIDE or LOCAL_GUIDE
    private String licensedNumber;
    private Integer yearsOfExperience;
    private List<String> specialization;
    private List<Language> languages;
    private String tourType; // GROUP or PRIVATE
    private String coveredArea;
    private PriceRange priceRange;
    private Integer tourDuration;
    private String profilePhoto;
    private String license;
    private String idDocument;
}
