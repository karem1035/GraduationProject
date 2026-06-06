package com.example.Toda.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "guide_booking_requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuideBookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(length = 2000)
    private String description;

    private Double price;

    @Enumerated(EnumType.STRING)
    private GuideBookingStatus status = GuideBookingStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourist_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserEntity tourist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_guide_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserEntity tourGuide;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum GuideBookingStatus {
        PENDING, ACCEPTED, REJECTED
    }
}