package com.example.Toda.service;

import com.example.Toda.DTO.BookingCreateRequest;
import com.example.Toda.DTO.BookingRequestResponse;
import com.example.Toda.DTO.TouristBookingResponse;
import com.example.Toda.Entity.*;
import com.example.Toda.repo.BookingRequestRepository;
import com.example.Toda.repo.TripRepository;
import com.example.Toda.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRequestRepository repository;
    private final UserRepo userRepo;
    private final TripRepository tripRepository;

    public BookingService(BookingRequestRepository repository, UserRepo userRepo, TripRepository tripRepository) {
        this.repository = repository;
        this.userRepo = userRepo;
        this.tripRepository = tripRepository;
    }

    // ==================== Guide Side ====================

    public List<BookingRequestResponse> getRequestsByStatus(String guideEmail, BookingRequest.RequestStatus status) {
        UserEntity user = userRepo.findByEmail(guideEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long guideId = user.getTourGuide().getId();

        List<BookingRequest> requests = repository.findByTourGuideIdAndStatus(guideId, status);

        return requests.stream()
                .map(this::convertToGuideResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStatus(Long requestId, BookingRequest.RequestStatus newStatus, String guideEmail) {
        BookingRequest request = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        String ownerEmail = request.getTourGuide().getUser().getEmail();
        if (!ownerEmail.equals(guideEmail)) {
            throw new RuntimeException("Unauthorized: You do not own this request");
        }

        request.setStatus(newStatus);
        repository.save(request);
    }

    // ==================== Tourist Side ====================

    /**
     * Create a new booking request from a tourist for a specific trip.
     */
    @Transactional
    public TouristBookingResponse createBookingRequest(BookingCreateRequest request, String touristEmail) {
        // Get the tourist
        UserEntity user = userRepo.findByEmail(touristEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTourristProfile() == null) {
            throw new RuntimeException("This user is not registered as a Tourist");
        }

        TouristProfileEntity tourist = user.getTourristProfile();

        // Get the trip
        Trip trip = tripRepository.findById(request.tripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        // Validate trip is bookable (must be UPCOMING)
        if (trip.getStatus() != TripStatus.UPCOMING) {
            throw new RuntimeException("This trip is not available for booking");
        }

        // Validate tourist count is within group size
        if (request.touristCount() != null) {
            if (trip.getMinGroupSize() != null && request.touristCount() < trip.getMinGroupSize()) {
                throw new RuntimeException("Tourist count is below the minimum group size of " + trip.getMinGroupSize());
            }
            if (trip.getMaxGroupSize() != null && request.touristCount() > trip.getMaxGroupSize()) {
                throw new RuntimeException("Tourist count exceeds the maximum group size of " + trip.getMaxGroupSize());
            }
        }

        // Check for duplicate pending request
        if (repository.existsByTouristIdAndTripIdAndStatus(tourist.getId(), trip.getId(), BookingRequest.RequestStatus.PENDING)) {
            throw new RuntimeException("You already have a pending booking request for this trip");
        }

        // Create the booking request
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setTourist(tourist);
        bookingRequest.setTourGuide(trip.getTourGuide());
        bookingRequest.setTrip(trip);
        bookingRequest.setCategory(request.category() != null ? request.category() :
                (trip.getCategories() != null && !trip.getCategories().isEmpty() ? trip.getCategories().get(0) : "General"));
        bookingRequest.setDate(request.date());
        bookingRequest.setTouristCount(request.touristCount());
        bookingRequest.setStatus(BookingRequest.RequestStatus.PENDING);

        bookingRequest = repository.save(bookingRequest);

        return convertToTouristResponse(bookingRequest);
    }

    /**
     * Get all booking requests for the authenticated tourist.
     */
    public List<TouristBookingResponse> getTouristBookings(String touristEmail) {
        UserEntity user = userRepo.findByEmail(touristEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTourristProfile() == null) {
            throw new RuntimeException("This user is not registered as a Tourist");
        }

        Long touristId = user.getTourristProfile().getId();

        List<BookingRequest> bookings = repository.findByTouristIdOrderByDateDesc(touristId);
        return bookings.stream()
                .map(this::convertToTouristResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get booking requests for a tourist filtered by status.
     */
    public List<TouristBookingResponse> getTouristBookingsByStatus(String touristEmail, BookingRequest.RequestStatus status) {
        UserEntity user = userRepo.findByEmail(touristEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTourristProfile() == null) {
            throw new RuntimeException("This user is not registered as a Tourist");
        }

        Long touristId = user.getTourristProfile().getId();

        List<BookingRequest> bookings = repository.findByTouristIdAndStatusOrderByDateDesc(touristId, status);
        return bookings.stream()
                .map(this::convertToTouristResponse)
                .collect(Collectors.toList());
    }

    /**
     * Cancel a booking request (tourist can cancel their own pending requests).
     */
    @Transactional
    public void cancelBookingRequest(Long requestId, String touristEmail) {
        BookingRequest request = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Booking request not found"));

        // Verify ownership
        String ownerEmail = request.getTourist().getUser().getEmail();
        if (!ownerEmail.equals(touristEmail)) {
            throw new RuntimeException("Unauthorized: You do not own this booking request");
        }

        // Can only cancel pending requests
        if (request.getStatus() != BookingRequest.RequestStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be cancelled");
        }

        request.setStatus(BookingRequest.RequestStatus.DECLINED);
        repository.save(request);
    }

    // ==================== Response Mappers ====================

    private BookingRequestResponse convertToGuideResponse(BookingRequest entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, h:mm a");
        String formattedDate = entity.getDate() != null ? entity.getDate().format(formatter) : "";

        Long tripId = entity.getTrip() != null ? entity.getTrip().getId() : null;
        String tripTitle = entity.getTrip() != null ? entity.getTrip().getTitle() : null;
        String tripCoverImage = entity.getTrip() != null ? entity.getTrip().getTripCoverImage() : null;

        return new BookingRequestResponse(
                entity.getId(),
                entity.getTourist().getUser().getUsername(),
                entity.getTourist().getProfilePhoto(),
                entity.getCategory(),
                formattedDate,
                entity.getTouristCount(),
                entity.getStatus().name(),
                tripId,
                tripTitle,
                tripCoverImage
        );
    }

    private TouristBookingResponse convertToTouristResponse(BookingRequest entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, h:mm a");
        String formattedDate = entity.getDate() != null ? entity.getDate().format(formatter) : "";

        Trip trip = entity.getTrip();
        TourGuideEntity guide = entity.getTourGuide();

        return new TouristBookingResponse(
                entity.getId(),
                entity.getStatus().name(),
                entity.getCategory(),
                formattedDate,
                entity.getTouristCount(),
                trip != null ? trip.getId() : null,
                trip != null ? trip.getTitle() : null,
                trip != null ? trip.getTripCoverImage() : null,
                trip != null ? trip.getCity() : null,
                trip != null ? trip.getTourDuration() : null,
                trip != null ? trip.getPricePerTourist() : null,
                guide != null ? guide.getId() : null,
                guide != null ? guide.getName() : null,
                guide != null ? guide.getProfilePhoto() : null
        );
    }
}