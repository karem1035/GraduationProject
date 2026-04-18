package com.example.Toda.service;

import com.example.Toda.DTO.*;
import com.example.Toda.Entity.TourGuideEntity;
import com.example.Toda.Entity.Trip;
import com.example.Toda.Entity.TripStatus;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.mapper.TripMapper;
import com.example.Toda.repo.TripRepository;
import com.example.Toda.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TripService {
    private final TripRepository tripRepository;
    @Autowired
    private TripMapper tripMapper;
    private final UserRepo userRepo;
    private final String uploadDir = "uploads/covers/";

    public TripService(TripRepository tripRepository, UserRepo userRepo) {
        this.tripRepository = tripRepository;
        this.userRepo = userRepo;
    }

    /**
     * Step 1: Create a new trip with basic info (title, categories, city, meeting point).
     * Returns the created trip ID so subsequent steps can update the same trip.
     */
    @Transactional
    public TripCreateResponse createCustomTrip(TripBasicInfoRequest request, String username) {
        TourGuideEntity guide = getTourGuideFromEmail(username);

        Trip trip = tripMapper.toEntity(request);
        trip.setTourGuide(guide);
        trip = tripRepository.save(trip);

        return new TripCreateResponse(trip.getId(), "Trip created successfully");
    }

    /**
     * Step 2: Add time details (dates, description, group sizes, duration) to an existing trip.
     */
    @Transactional
    public void addTripTime(Long tripId, TripInfoTimeRequest request, String username) {
        TourGuideEntity guide = getTourGuideFromEmail(username);
        Trip trip = getTripOwnedByGuide(tripId, guide);

        tripMapper.updateTripFromTimeRequest(request, trip);
        tripRepository.save(trip);
    }

    /**
     * Step 3: Add pricing details (price per tourist, inclusions) to an existing trip.
     */
    @Transactional
    public void addTripPrice(Long tripId, TripInfoPriceRequest request, String username) {
        TourGuideEntity guide = getTourGuideFromEmail(username);
        Trip trip = getTripOwnedByGuide(tripId, guide);

        tripMapper.updateTripFromPriceRequest(request, trip);
        tripRepository.save(trip);
    }

    /**
     * Upload a cover image for a trip.
     */
    @Transactional
    public String saveTripCover(Long tripId, MultipartFile file, String username) {
        TourGuideEntity guide = getTourGuideFromEmail(username);
        Trip trip = getTripOwnedByGuide(tripId, guide);

        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            trip.setTripCoverImage(path.toString());
            tripRepository.save(trip);

            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    /**
     * Get a guide's trips filtered by optional status.
     */
    public List<TripCardResponse> getFilteredTrips(String email, String status) {
        List<Trip> trips = tripRepository.findByEmailAndOptionalStatus(email, TripStatus.valueOf(status));
        return tripMapper.toCardResponseList(trips);
    }

    /**
     * Get a single trip by ID (used internally and for public viewing).
     */
    public Trip getTripById(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));
    }

    /**
     * Get full trip details by ID (for public viewing).
     * Includes guide information.
     */
    public TripDetailsResponse getTripDetails(Long tripId) {
        Trip trip = getTripById(tripId);
        return mapToDetailsResponse(trip);
    }

    /**
     * Get all published/upcoming trips with pagination.
     */
    public Page<TripCardResponse> getAllPublishedTrips(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Trip> trips = tripRepository.findByStatus(TripStatus.UPCOMING, pageable);
        return trips.map(tripMapper::toCardResponse);
    }

    /**
     * Search trips with optional filters.
     */
    public Page<TripCardResponse> searchTrips(
            String city,
            String category,
            LocalDate startDate,
            LocalDate endDate,
            Double minPrice,
            Double maxPrice,
            Integer groupSize,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Trip> trips = tripRepository.searchTrips(
                TripStatus.UPCOMING, city, category, startDate, endDate,
                minPrice, maxPrice, groupSize, pageable);
        return trips.map(tripMapper::toCardResponse);
    }

    /**
     * Get trips by a specific guide (for public guide profile).
     */
    public Page<TripCardResponse> getTripsByGuide(Long guideId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Trip> trips = tripRepository.findByTourGuideIdAndStatus(guideId, TripStatus.UPCOMING, pageable);
        return trips.map(tripMapper::toCardResponse);
    }

    /**
     * Update the status of a trip (e.g., NEW → UPCOMING → COMPLETED or CANCELLED).
     * Only the trip owner (guide) can update the status.
     */
    @Transactional
    public TripCardResponse updateTripStatus(Long tripId, TripStatus newStatus, String guideEmail) {
        TourGuideEntity guide = getTourGuideFromEmail(guideEmail);
        Trip trip = getTripOwnedByGuide(tripId, guide);

        // Validate status transition
        validateStatusTransition(trip.getStatus(), newStatus);

        trip.setStatus(newStatus);
        trip = tripRepository.save(trip);

        return tripMapper.toCardResponse(trip);
    }

    /**
     * Validate that the status transition is allowed.
     */
    private void validateStatusTransition(TripStatus current, TripStatus target) {
        if (current == target) {
            throw new RuntimeException("Trip is already in " + current.name() + " status");
        }

        if (target == TripStatus.NEW) {
            throw new RuntimeException("Cannot change trip status back to NEW");
        }

        if (current == TripStatus.COMPLETED) {
            throw new RuntimeException("Cannot change status of a completed trip");
        }

        if (current == TripStatus.CANCELLED) {
            throw new RuntimeException("Cannot change status of a cancelled trip");
        }
    }

    // ==================== Helper Methods ====================

    /**
     * Map Trip entity to TripDetailsResponse including guide info.
     */
    private TripDetailsResponse mapToDetailsResponse(Trip trip) {
        TourGuideEntity guide = trip.getTourGuide();

        TripDetailsResponse.GuideInfo guideInfo = new TripDetailsResponse.GuideInfo(
                guide.getId(),
                guide.getName(),
                guide.getProfilePhoto(),
                guide.getCity(),
                null, // rating - not implemented yet
                guide.getYearsOfExperience()
        );

        return new TripDetailsResponse(
                trip.getId(),
                trip.getTitle(),
                trip.getCity(),
                trip.getMeetingPoint(),
                trip.getDescription(),
                trip.getMinGroupSize(),
                trip.getMaxGroupSize(),
                trip.getTourDuration(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getPricePerTourist(),
                trip.getStatus() != null ? trip.getStatus().name() : "NEW",
                trip.getTripCoverImage(),
                trip.getCategories(),
                trip.getInclusions(),
                guideInfo
        );
    }

    private TourGuideEntity getTourGuideFromEmail(String email) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTourGuide() == null) {
            throw new RuntimeException("This user is not registered as a Tour Guide");
        }
        return user.getTourGuide();
    }

    private Trip getTripOwnedByGuide(Long tripId, TourGuideEntity guide) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        if (!trip.getTourGuide().getId().equals(guide.getId())) {
            throw new RuntimeException("Unauthorized: You do not own this trip");
        }
        return trip;
    }
}