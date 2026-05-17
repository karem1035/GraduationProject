package com.example.Toda.repo;

import com.example.Toda.Entity.Trip;
import com.example.Toda.Entity.TripStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByTourGuideIdOrderByStartDateAsc(Long guideId);

    List<Trip> findByTourGuideIdAndStartDateAfterOrderByStartDateAsc(Long guideId, LocalDate date);

    @Query("SELECT t FROM Trip t WHERE t.tourGuide.email = :email " +
            "AND (:status IS NULL OR t.status = :status)")
    List<Trip> findByEmailAndOptionalStatus(
            @Param("email") String email,
            @Param("status") TripStatus status);

    /**
     * Get all published/upcoming trips with pagination.
     */
    Page<Trip> findByStatus(TripStatus status, Pageable pageable);

    /**
     * Search trips with optional filters: city, category, date range, group size.
     */
    @Query("SELECT DISTINCT t FROM Trip t " +
            "LEFT JOIN t.categories c " +
            "WHERE t.status = :status " +
            "AND (:city IS NULL OR LOWER(t.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
            "AND (:category IS NULL OR LOWER(c) LIKE LOWER(CONCAT('%', :category, '%'))) " +
            "AND (:startDate IS NULL OR t.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR t.endDate <= :endDate) " +
            "AND (:minPrice IS NULL OR t.pricePerTourist >= :minPrice) " +
            "AND (:maxPrice IS NULL OR t.pricePerTourist <= :maxPrice) " +
            "AND (:groupSize IS NULL OR (t.minGroupSize <= :groupSize AND t.maxGroupSize >= :groupSize))")
    Page<Trip> searchTrips(
            @Param("status") TripStatus status,
            @Param("city") String city,
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("groupSize") Integer groupSize,
            Pageable pageable);

    /**
     * Get all trips by a specific tour guide (for public profile viewing).
     */
    Page<Trip> findByTourGuideIdAndStatus(Long guideId, TripStatus status, Pageable pageable);

    /**
     * Get all published trips that include a specific landmark.
     */
    Page<Trip> findByLandmarksIdAndStatus(Long landmarkId, TripStatus status, Pageable pageable);
}
