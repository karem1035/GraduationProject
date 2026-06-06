package com.example.Toda.repo;

import com.example.Toda.Entity.GuideBookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuideBookingRequestRepository extends JpaRepository<GuideBookingRequest, Long> {

    List<GuideBookingRequest> findByTourist_IdOrderByCreatedAtDesc(Long touristId);

    List<GuideBookingRequest> findByTourGuide_IdOrderByCreatedAtDesc(Long guideId);

    List<GuideBookingRequest> findByTourGuide_IdAndStatusOrderByCreatedAtDesc(Long guideId, GuideBookingRequest.GuideBookingStatus status);
}