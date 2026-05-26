package com.example.Toda.service;

import com.example.Toda.DTO.*;
import com.example.Toda.Entity.Landmark;
import com.example.Toda.Entity.LandmarkType;
import com.example.Toda.Entity.Trip;
import com.example.Toda.Entity.TripStatus;
import com.example.Toda.repo.LandmarkRepository;
import com.example.Toda.repo.TripRepository;
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
import java.util.List;
import java.util.UUID;

@Service
public class LandmarkService {

    private final LandmarkRepository landmarkRepository;
    private final TripRepository tripRepository;
    private final String uploadDir = "uploads/landmarks/";

    public LandmarkService(LandmarkRepository landmarkRepository, TripRepository tripRepository) {
        this.landmarkRepository = landmarkRepository;
        this.tripRepository = tripRepository;
    }

    // ==================== CRUD Operations ====================

    /**
     * Create a new landmark.
     */
    @Transactional
    public LandmarkResponse createLandmark(LandmarkCreateRequest request) {
        Landmark landmark = new Landmark();
        landmark.setName(request.name());
        landmark.setDescription(request.description());
        landmark.setCity(request.city());
        landmark.setAddress(request.address());
        landmark.setType(LandmarkType.valueOf(request.type()));

        landmark = landmarkRepository.save(landmark);
        return toResponse(landmark);
    }

    /**
     * Update an existing landmark.
     */
    @Transactional
    public LandmarkResponse updateLandmark(Long id, LandmarkUpdateRequest request) {
        Landmark landmark = getLandmarkById(id);

        if (request.name() != null) landmark.setName(request.name());
        if (request.description() != null) landmark.setDescription(request.description());
        if (request.city() != null) landmark.setCity(request.city());
        if (request.address() != null) landmark.setAddress(request.address());
        if (request.type() != null) landmark.setType(LandmarkType.valueOf(request.type()));

        landmark = landmarkRepository.save(landmark);
        return toResponse(landmark);
    }

    /**
     * Delete a landmark.
     */
    @Transactional
    public void deleteLandmark(Long id) {
        Landmark landmark = getLandmarkById(id);
        landmarkRepository.delete(landmark);
    }

    /**
     * Upload an image for a landmark.
     */
    @Transactional
    public String uploadLandmarkImage(Long id, MultipartFile file) {
        Landmark landmark = getLandmarkById(id);

        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            landmark.setImageUrl(path.toString());
            landmarkRepository.save(landmark);

            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    // ==================== Read Operations ====================

    /**
     * Get a single landmark by ID.
     */
    public LandmarkResponse getLandmarkDetails(Long id) {
        Landmark landmark = getLandmarkById(id);
        return toResponse(landmark);
    }

    /**
     * Get all landmarks with optional filters and pagination.
     */
    public Page<LandmarkCardResponse> getFilteredLandmarks(LandmarkType type, String city, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<Landmark> landmarks = landmarkRepository.findFiltered(type, city, name, pageable);
        return landmarks.map(this::toCardResponse);
    }

    /**
     * Get all trips that include a specific landmark.
     */
    public Page<TripCardResponse> getTripsByLandmark(Long landmarkId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Trip> trips = tripRepository.findByLandmarksIdAndStatus(landmarkId, TripStatus.UPCOMING, pageable);
        return trips.map(this::toTripCardResponse);
    }

    // ==================== Trip-Landmark Association ====================

    /**
     * Attach landmarks to a trip.
     */
    @Transactional
    public ApiResponse<String> attachLandmarksToTrip(Long tripId, TripLandmarkRequest request, String guideEmail) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        List<Landmark> landmarks = landmarkRepository.findByIdIn(request.landmarkIds());
        if (landmarks.size() != request.landmarkIds().size()) {
            throw new RuntimeException("One or more landmarks not found");
        }

        List<Landmark> currentLandmarks = trip.getLandmarks();
        if (currentLandmarks == null) {
            trip.setLandmarks(landmarks);
        } else {
            for (Landmark landmark : landmarks) {
                if (!currentLandmarks.contains(landmark)) {
                    currentLandmarks.add(landmark);
                }
            }
        }

        tripRepository.save(trip);
        return ApiResponse.success("Landmarks attached to trip successfully", null);
    }

    /**
     * Remove a landmark from a trip.
     */
    @Transactional
    public ApiResponse<String> removeLandmarkFromTrip(Long tripId, Long landmarkId, String guideEmail) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        Landmark landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new RuntimeException("Landmark not found"));

        if (trip.getLandmarks() != null) {
            trip.getLandmarks().remove(landmark);
            tripRepository.save(trip);
        }

        return ApiResponse.success("Landmark removed from trip successfully", null);
    }

    // ==================== Helper Methods ====================

    private Landmark getLandmarkById(Long id) {
        return landmarkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Landmark not found"));
    }

    private LandmarkResponse toResponse(Landmark landmark) {
        return new LandmarkResponse(
                landmark.getId(),
                landmark.getName(),
                landmark.getDescription(),
                landmark.getCity(),
                landmark.getAddress(),
                landmark.getType() != null ? landmark.getType().name() : null,
                landmark.getImageUrl()
        );
    }

    private LandmarkCardResponse toCardResponse(Landmark landmark) {
        return new LandmarkCardResponse(
                landmark.getId(),
                landmark.getName(),
                landmark.getCity(),
                landmark.getType() != null ? landmark.getType().name() : null,
                landmark.getImageUrl()
        );
    }

    private TripCardResponse toTripCardResponse(Trip trip) {
        String category = "General";
        if (trip.getCategories() != null && !trip.getCategories().isEmpty()) {
            category = trip.getCategories().get(0);
        }
        String status = trip.getStatus() != null ? trip.getStatus().name() : "NEW";

        return new TripCardResponse(
                trip.getId(),
                trip.getTitle(),
                trip.getCity(),
                category,
                trip.getTripCoverImage(),
                trip.getTourDuration(),
                status,
                trip.getPricePerTourist(),
                trip.getStartDate(),
                trip.getEndDate()
        );
    }
}