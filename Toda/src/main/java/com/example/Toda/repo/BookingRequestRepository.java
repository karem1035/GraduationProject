package com.example.Toda.repo;

import com.example.Toda.Entity.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {

    List<BookingRequest> findByTourGuideIdAndStatus(Long guideId, BookingRequest.RequestStatus status);

    long countByTourGuideIdAndStatus(Long guideId, BookingRequest.RequestStatus status);

    /**
     * Get all booking requests for a specific tourist.
     */
    List<BookingRequest> findByTouristIdOrderByDateDesc(Long touristId);

    /**
     * Get booking requests for a tourist filtered by status.
     */
    List<BookingRequest> findByTouristIdAndStatusOrderByDateDesc(Long touristId, BookingRequest.RequestStatus status);

    /**
     * Get booking requests for a specific trip.
     */
    List<BookingRequest> findByTripId(Long tripId);

    /**
     * Check if a tourist already has a pending request for a specific trip.
     */
    boolean existsByTouristIdAndTripIdAndStatus(Long touristId, Long tripId, BookingRequest.RequestStatus status);
}