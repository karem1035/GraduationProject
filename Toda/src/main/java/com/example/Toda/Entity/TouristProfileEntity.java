package com.example.Toda.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tourist_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TouristProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TouristType type;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "mother_language", nullable = false)
    private String motherLanguage;

    @ElementCollection
    @CollectionTable(name = "tourist_languages", joinColumns = @JoinColumn(name = "tourist_profile_id"))
    @Column(name = "language")
    private List<String> languages;

    @Column(name = "travel_date_from")
    private LocalDate travelDateFrom;

    @Column(name = "travel_date_to")
    private LocalDate travelDateTo;

    @Column(name = "destination_city", nullable = false)
    private String destinationCity;

    @Column(name = "trip_type", nullable = false)
    private String tripType;

    @Column(name = "number_of_travelers", nullable = false)
    private Integer numberOfTravelers;

    @ElementCollection
    @CollectionTable(name = "tourist_interests", joinColumns = @JoinColumn(name = "tourist_profile_id"))
    @Column(name = "interest")
    private List<String> travelInterests;

    @ElementCollection
    @CollectionTable(name = "tourist_special_needs", joinColumns = @JoinColumn(name = "tourist_profile_id"))
    @Column(name = "special_need")
    private List<String> specialNeeds;

    @ElementCollection
    @CollectionTable(name = "tourist_travel_preferences", joinColumns = @JoinColumn(name = "tourist_profile_id"))
    @Column(name = "preference")
    private List<String> travelPreferences;

    @ElementCollection
    @CollectionTable(name = "tourist_food_preferences", joinColumns = @JoinColumn(name = "tourist_profile_id"))
    @Column(name = "food_preference")
    private List<String> foodPreferences;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public enum TouristType {
        MALE,
        FEMALE
    }
}
