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
@Table(name = "static_trips")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaticTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String city;

    private String meetingPoint;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double price;

    private String duration;

    private Integer groupSize;

    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "static_trip_categories", joinColumns = @JoinColumn(name = "trip_id"))
    @Column(name = "category")
    private List<String> categories = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "static_trip_inclusions", joinColumns = @JoinColumn(name = "trip_id"))
    @Column(name = "inclusion")
    private List<String> inclusions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserEntity createdBy;
}