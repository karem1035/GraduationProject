package com.example.Toda.service;

import com.example.Toda.DTO.ApiResponse;
import com.example.Toda.DTO.LandmarkCardResponse;
import com.example.Toda.DTO.TripCardResponse;
import com.example.Toda.Entity.Landmark;
import com.example.Toda.Entity.Trip;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.repo.LandmarkRepository;
import com.example.Toda.repo.TripRepository;
import com.example.Toda.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class FavoriteService {

    private final UserRepo userRepo;
    private final TripRepository tripRepository;
    private final LandmarkRepository landmarkRepository;
    private final JWTService jwtService;

    public FavoriteService(UserRepo userRepo, TripRepository tripRepository,
                           LandmarkRepository landmarkRepository, JWTService jwtService) {
        this.userRepo = userRepo;
        this.tripRepository = tripRepository;
        this.landmarkRepository = landmarkRepository;
        this.jwtService = jwtService;
    }

    // ==================== Trip Favorites ====================

    @Transactional
    public ApiResponse<String> addTripToFavorites(String token, Long tripId) {
        UserEntity user = getUserFromToken(token);
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        user.getFavoriteTrips().add(trip);
        userRepo.save(user);
        return ApiResponse.success("Trip added to favorites", null);
    }

    @Transactional
    public ApiResponse<String> removeTripFromFavorites(String token, Long tripId) {
        UserEntity user = getUserFromToken(token);
        user.getFavoriteTrips().removeIf(t -> t.getId().equals(tripId));
        userRepo.save(user);
        return ApiResponse.success("Trip removed from favorites", null);
    }

    public ApiResponse<List<TripCardResponse>> getFavoriteTrips(String token) {
        UserEntity user = getUserFromToken(token);
        Set<Trip> favorites = user.getFavoriteTrips();

        List<TripCardResponse> trips = favorites.stream()
                .map(this::toTripCardResponse)
                .toList();
        return ApiResponse.success("Favorite trips retrieved successfully", trips);
    }

    // ==================== Landmark Favorites ====================

    @Transactional
    public ApiResponse<String> addLandmarkToFavorites(String token, Long landmarkId) {
        UserEntity user = getUserFromToken(token);
        Landmark landmark = landmarkRepository.findById(landmarkId)
                .orElseThrow(() -> new RuntimeException("Landmark not found"));

        user.getFavoriteLandmarks().add(landmark);
        userRepo.save(user);
        return ApiResponse.success("Landmark added to favorites", null);
    }

    @Transactional
    public ApiResponse<String> removeLandmarkFromFavorites(String token, Long landmarkId) {
        UserEntity user = getUserFromToken(token);
        user.getFavoriteLandmarks().removeIf(l -> l.getId().equals(landmarkId));
        userRepo.save(user);
        return ApiResponse.success("Landmark removed from favorites", null);
    }

    public ApiResponse<List<LandmarkCardResponse>> getFavoriteLandmarks(String token) {
        UserEntity user = getUserFromToken(token);
        Set<Landmark> favorites = user.getFavoriteLandmarks();

        List<LandmarkCardResponse> landmarks = favorites.stream()
                .map(this::toLandmarkCardResponse)
                .toList();
        return ApiResponse.success("Favorite landmarks retrieved successfully", landmarks);
    }

    // ==================== Helpers ====================

    private UserEntity getUserFromToken(String token) {
        String email = jwtService.extractUsername(token);
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
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

    private LandmarkCardResponse toLandmarkCardResponse(Landmark landmark) {
        return new LandmarkCardResponse(
                landmark.getId(),
                landmark.getName(),
                landmark.getCity(),
                landmark.getType() != null ? landmark.getType().name() : null,
                landmark.getImageUrl()
        );
    }
}