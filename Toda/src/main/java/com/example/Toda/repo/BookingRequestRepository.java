package com.example.Toda.repo;


import com.example.Toda.Entity.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {

    List<BookingRequest> findByTourGuideIdAndStatus(Long guideId, BookingRequest.RequestStatus status);

    long countByTourGuideIdAndStatus(Long guideId, BookingRequest.RequestStatus status);

}
