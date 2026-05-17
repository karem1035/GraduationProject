package com.example.Toda.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "landmarks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Landmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;

    private String city;

    private String address;

    @Enumerated(EnumType.STRING)
    private LandmarkType type;

    private String imageUrl;

    @ManyToMany(mappedBy = "landmarks", fetch = FetchType.LAZY)
    private List<Trip> trips;
}