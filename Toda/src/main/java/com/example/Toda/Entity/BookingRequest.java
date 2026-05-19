package com.example.Toda.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourist_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TouristProfileEntity tourist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_guide_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TourGuideEntity tourGuide;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Trip trip;

    private String category;
    private LocalDateTime date;
    private Integer touristCount;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    public enum RequestStatus {
        PENDING, ACCEPTED, DECLINED, COMPLETED
    }
}