package com.example.Toda.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tourist_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TouristProfileEntity {
    @Id
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserEntity user;
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

    @Column(name = "destination_city")
    private String destinationCity;

    @Column(name = "trip_type")
    private String tripType;

    @Column(name = "number_of_travelers")
    private Integer numberOfTravelers;

    @ElementCollection
    @CollectionTable(name = "tourist_interests", joinColumns = @JoinColumn(name = "tourist_profile_id"))
    @Column(name = "interest")
    private List<String> travelInterests;

    @Column(name = "special_needs")
    private String specialNeeds;

    @ElementCollection
    @CollectionTable(name = "tourist_travel_preferences", joinColumns = @JoinColumn(name = "tourist_profile_id"))
    @Column(name = "preference")
    private List<String> travelPreferences;

    @Column(name = "food_preference")
    private String foodPreference;

    @Column(name = "food_allergies", columnDefinition = "TEXT")
    private String foodAllergies;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "phone")
    private String phone;

    public enum TouristType {
        MALE,
        FEMALE
    }
}
