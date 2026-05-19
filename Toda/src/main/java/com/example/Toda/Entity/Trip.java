package com.example.Toda.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trips")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String city;
    private String meetingPoint;

    @Column(length = 1000)
    private String description;
    private Integer minGroupSize;
    private Integer maxGroupSize;
    private String tourDuration;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double pricePerTourist;
    @Enumerated(EnumType.STRING)
    private TripStatus status;
    private String tripCoverImage;

    @ElementCollection
    @CollectionTable(name = "trip_categories", joinColumns = @JoinColumn(name = "trip_id"))
    private List<String> categories;

    @ElementCollection
    @CollectionTable(name = "trip_inclusions", joinColumns = @JoinColumn(name = "trip_id"))
    private List<String> inclusions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_guide_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TourGuideEntity tourGuide;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trip_landmarks",
            joinColumns = @JoinColumn(name = "trip_id"),
            inverseJoinColumns = @JoinColumn(name = "landmark_id")
    )
    private List<Landmark> landmarks = new ArrayList<>();
}
