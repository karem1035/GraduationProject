package com.example.Toda.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TouristProfileResponse {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String type; // MALE or FEMALE
    private String nationality;
    private String motherLanguage;
    private List<String> languages;
    private LocalDate travelDateFrom;
    private LocalDate travelDateTo;
    private String destinationCity;
    private String tripType;
    private Integer numberOfTravelers;
    private List<String> travelInterests;
    private List<String> specialNeeds;
    private List<String> travelPreferences;
    private List<String> foodPreferences;
    private String notes;
}
