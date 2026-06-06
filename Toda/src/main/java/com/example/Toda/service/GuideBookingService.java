package com.example.Toda.service;

import com.example.Toda.DTO.GuideBookingCreateRequest;
import com.example.Toda.DTO.GuideBookingResponse;
import com.example.Toda.Entity.GuideBookingRequest;
import com.example.Toda.Entity.TourGuideEntity;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.repo.GuideBookingRequestRepository;
import com.example.Toda.repo.TourGuideRepo;
import com.example.Toda.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuideBookingService {

    private final GuideBookingRequestRepository guideBookingRequestRepository;
    private final UserRepo userRepo;
    private final TourGuideRepo tourGuideRepo;

    public GuideBookingService(GuideBookingRequestRepository guideBookingRequestRepository,
                               UserRepo userRepo,
                               TourGuideRepo tourGuideRepo) {
        this.guideBookingRequestRepository = guideBookingRequestRepository;
        this.userRepo = userRepo;
        this.tourGuideRepo = tourGuideRepo;
    }

    @Transactional
    public GuideBookingResponse createBookingRequest(GuideBookingCreateRequest request, String touristEmail) {
        UserEntity tourist = userRepo.findByEmail(touristEmail)
                .orElseThrow(() -> new RuntimeException("Tourist not found"));

        UserEntity guide = userRepo.findById(request.tourGuideId())
                .orElseThrow(() -> new RuntimeException("Tour guide not found"));

        // Verify the target user is actually a tour guide
        if (guide.getTourGuide() == null) {
            throw new RuntimeException("The specified user is not a tour guide");
        }

        GuideBookingRequest booking = new GuideBookingRequest();
        booking.setTitle(request.title());
        booking.setStartDate(request.startDate());
        booking.setEndDate(request.endDate());
        booking.setDescription(request.description());
        booking.setPrice(request.price());
        booking.setTourist(tourist);
        booking.setTourGuide(guide);
        booking.setStatus(GuideBookingRequest.GuideBookingStatus.PENDING);

        GuideBookingRequest saved = guideBookingRequestRepository.save(booking);
        return mapToResponse(saved);
    }

    public List<GuideBookingResponse> getTouristBookings(String touristEmail) {
        UserEntity tourist = userRepo.findByEmail(touristEmail)
                .orElseThrow(() -> new RuntimeException("Tourist not found"));

        return guideBookingRequestRepository.findByTourist_IdOrderByCreatedAtDesc(tourist.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<GuideBookingResponse> getGuideBookings(String guideEmail) {
        UserEntity guide = userRepo.findByEmail(guideEmail)
                .orElseThrow(() -> new RuntimeException("Tour guide not found"));

        return guideBookingRequestRepository.findByTourGuide_IdOrderByCreatedAtDesc(guide.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<GuideBookingResponse> getGuideBookingsByStatus(String guideEmail, GuideBookingRequest.GuideBookingStatus status) {
        UserEntity guide = userRepo.findByEmail(guideEmail)
                .orElseThrow(() -> new RuntimeException("Tour guide not found"));

        return guideBookingRequestRepository.findByTourGuide_IdAndStatusOrderByCreatedAtDesc(guide.getId(), status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public GuideBookingResponse acceptBooking(Long bookingId, String guideEmail) {
        GuideBookingRequest booking = guideBookingRequestRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking request not found"));

        UserEntity guide = userRepo.findByEmail(guideEmail)
                .orElseThrow(() -> new RuntimeException("Tour guide not found"));

        if (!booking.getTourGuide().getId().equals(guide.getId())) {
            throw new RuntimeException("You can only accept bookings addressed to you");
        }

        if (booking.getStatus() != GuideBookingRequest.GuideBookingStatus.PENDING) {
            throw new RuntimeException("Only pending bookings can be accepted");
        }

        booking.setStatus(GuideBookingRequest.GuideBookingStatus.ACCEPTED);
        GuideBookingRequest saved = guideBookingRequestRepository.save(booking);
        return mapToResponse(saved);
    }

    @Transactional
    public GuideBookingResponse rejectBooking(Long bookingId, String guideEmail) {
        GuideBookingRequest booking = guideBookingRequestRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking request not found"));

        UserEntity guide = userRepo.findByEmail(guideEmail)
                .orElseThrow(() -> new RuntimeException("Tour guide not found"));

        if (!booking.getTourGuide().getId().equals(guide.getId())) {
            throw new RuntimeException("You can only reject bookings addressed to you");
        }

        if (booking.getStatus() != GuideBookingRequest.GuideBookingStatus.PENDING) {
            throw new RuntimeException("Only pending bookings can be rejected");
        }

        booking.setStatus(GuideBookingRequest.GuideBookingStatus.REJECTED);
        GuideBookingRequest saved = guideBookingRequestRepository.save(booking);
        return mapToResponse(saved);
    }

    private GuideBookingResponse mapToResponse(GuideBookingRequest booking) {
        GuideBookingResponse.UserInfo touristInfo = null;
        if (booking.getTourist() != null) {
            String touristPhoto = null;
            String touristName = booking.getTourist().getUsername();
            if (booking.getTourist().getTourristProfile() != null) {
                touristName = booking.getTourist().getTourristProfile().getName();
            }
            touristInfo = new GuideBookingResponse.UserInfo(
                    booking.getTourist().getId(),
                    touristName,
                    touristPhoto
            );
        }

        GuideBookingResponse.UserInfo guideInfo = null;
        if (booking.getTourGuide() != null) {
            String guidePhoto = null;
            String guideName = booking.getTourGuide().getUsername();
            if (booking.getTourGuide().getTourGuide() != null) {
                guidePhoto = booking.getTourGuide().getTourGuide().getProfilePhoto();
                guideName = booking.getTourGuide().getTourGuide().getName();
            }
            guideInfo = new GuideBookingResponse.UserInfo(
                    booking.getTourGuide().getId(),
                    guideName,
                    guidePhoto
            );
        }

        return new GuideBookingResponse(
                booking.getId(),
                booking.getTitle(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getDescription(),
                booking.getPrice(),
                booking.getStatus().name(),
                booking.getCreatedAt(),
                touristInfo,
                guideInfo
        );
    }
}