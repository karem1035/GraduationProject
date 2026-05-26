package com.example.Toda.DTO;

import com.example.Toda.Entity.Language;
import com.example.Toda.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithProfileResponse {
    private UserData user;
    private TourGuideProfileData tourGuideProfile;
    private TouristProfileData touristProfile;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserData {
        private Long id;
        private String username;
        private String email;
        private Role role;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TourGuideProfileData {
        private Long id;
        private String name;
        private String email;
        private String city;
        private String phone;
        private String licensedNumber;
        private Integer yearsOfExperience;
        private String guideType;
        private String tourType;
        private String coveredArea;
        private Integer tourDuration;
        private List<Language> languages;
        private String profilePhoto;
        private String license;
        private String idDocument;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TouristProfileData {
        private Long id;
        private String name;
        private String email;
        private String type;
        private String nationality;
        private String motherLanguage;
        private List<String> languages;
        private String phone;
        private String profilePhoto;
        private LocalDate travelDateFrom;
        private LocalDate travelDateTo;
        private String destinationCity;
        private String tripType;
        private Integer numberOfTravelers;
        private List<String> travelInterests;
        private String specialNeeds;
        private List<String> travelPreferences;
        private String foodPreference;
        private String foodAllergies;
        private String notes;
    }
}