package com.example.Toda.Entity;

import com.example.Toda.DTO.Language;
import com.example.Toda.DTO.PriceRange;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tour_guide")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourGuideEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private GuideType type;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "city", nullable = false)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "guide_type", nullable = false)
    private GuideTypeCategory guideType;

    @Column(name = "licensed_number")
    private String licensedNumber;

    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    @ElementCollection
    @CollectionTable(name = "tour_guide_specialization", joinColumns = @JoinColumn(name = "tour_guide_id"))
    @Column(name = "specialization")
    private List<String> specialization;

    @ElementCollection
    @CollectionTable(name = "tour_guide_languages", joinColumns = @JoinColumn(name = "tour_guide_id"))
    private List<Language> languages;

    @Enumerated(EnumType.STRING)
    @Column(name = "tour_type", nullable = false)
    private TourType tourType;

    @Column(name = "covered_area", nullable = false)
    private String coveredArea;

    @Embedded
    private PriceRange priceRange;

    @Column(name = "tour_duration", nullable = false)
    private Integer tourDuration;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "license")
    private String license;

    @Column(name = "id_document")
    private String idDocument;

    public enum GuideType {
        MALE,
        FEMALE
    }

    public enum GuideTypeCategory {
        LICENSED_GUIDE,
        LOCAL_GUIDE
    }

    public enum TourType {
        GROUP,
        PRIVATE
    }
}
