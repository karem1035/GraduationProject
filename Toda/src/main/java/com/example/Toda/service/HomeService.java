package com.example.Toda.service;

import com.example.Toda.DTO.BookingRequestResponse;
import com.example.Toda.DTO.HomeDashboardResponse;
import com.example.Toda.Entity.BookingRequest;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.repo.BookingRequestRepository;
import com.example.Toda.repo.TourGuideRepo;
import com.example.Toda.repo.TripRepository;
import com.example.Toda.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class HomeService {
    @Autowired private TourGuideRepo guideRepo;
    @Autowired private TripRepository tripRepo;
    @Autowired private BookingRequestRepository requestRepo;
    @Autowired private UserRepo userRepo;

    private final String SERVER_URL = "http://localhost:8080";

    public HomeDashboardResponse getDashboardDataByEmail(String email) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTourGuide() == null) {
            throw new RuntimeException("User is not a Tour Guide");
        }

        Long guideId = user.getTourGuide().getId();
        var guide = guideRepo.findById(guideId).orElseThrow();


        String fullProfilePath = guide.getProfilePhoto() != null ? SERVER_URL + guide.getProfilePhoto() : null;
        var trips = tripRepo.findByTourGuideIdOrderByStartDateAsc(guideId)
                .stream().map(t -> new HomeDashboardResponse.TripResponse(
                        t.getId(),
                        t.getTitle(),
                        t.getTripCoverImage() != null ? SERVER_URL + t.getTripCoverImage() : null,
                        t.getStartDate() != null ? t.getStartDate().toString() : null,
                        t.getCity()))
                .limit(5).toList();

        var requests = requestRepo.findByTourGuideIdAndStatus(guideId, BookingRequest.RequestStatus.PENDING)
                .stream().limit(2).map(this::convertToResponse).toList();

        long completed = requestRepo.countByTourGuideIdAndStatus(guideId, BookingRequest.RequestStatus.COMPLETED);

        var stats = new HomeDashboardResponse.MonthlyStats(completed, 4.8, 1200.0);

        return new HomeDashboardResponse(
                guide.getName(),
                guide.getCity(),
                fullProfilePath,
                trips,
                requests,
                stats
        );
    }

    private BookingRequestResponse convertToResponse(BookingRequest entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, h:mm a");
        String formattedDate = entity.getDate() != null ? entity.getDate().format(formatter) : "";

        String touristPhoto = entity.getTourist().getProfilePhoto() != null ?
                SERVER_URL + entity.getTourist().getProfilePhoto() : null;

        return new BookingRequestResponse(
                entity.getId(),
                entity.getTourist().getUser().getUsername(),
                touristPhoto,
                entity.getCategory(),
                formattedDate,
                entity.getTouristCount(),
                entity.getStatus().name()
        );
    }
}