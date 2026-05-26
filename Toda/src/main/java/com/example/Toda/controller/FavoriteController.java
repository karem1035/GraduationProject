package com.example.Toda.controller;

import com.example.Toda.DTO.ApiResponse;
import com.example.Toda.DTO.LandmarkCardResponse;
import com.example.Toda.DTO.TripCardResponse;
import com.example.Toda.service.FavoriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorites", description = "Manage favorite trips and landmarks")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // ==================== Trip Favorites ====================

    @PostMapping("/trips/{tripId}")
    public ResponseEntity<ApiResponse<String>> addTripToFavorites(
            @PathVariable Long tripId,
            @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseEntity.ok(favoriteService.addTripToFavorites(jwt, tripId));
    }

    @DeleteMapping("/trips/{tripId}")
    public ResponseEntity<ApiResponse<String>> removeTripFromFavorites(
            @PathVariable Long tripId,
            @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseEntity.ok(favoriteService.removeTripFromFavorites(jwt, tripId));
    }

    @GetMapping("/trips")
    public ResponseEntity<ApiResponse<List<TripCardResponse>>> getFavoriteTrips(
            @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseEntity.ok(favoriteService.getFavoriteTrips(jwt));
    }

    // ==================== Landmark Favorites ====================

    @PostMapping("/landmarks/{landmarkId}")
    public ResponseEntity<ApiResponse<String>> addLandmarkToFavorites(
            @PathVariable Long landmarkId,
            @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseEntity.ok(favoriteService.addLandmarkToFavorites(jwt, landmarkId));
    }

    @DeleteMapping("/landmarks/{landmarkId}")
    public ResponseEntity<ApiResponse<String>> removeLandmarkFromFavorites(
            @PathVariable Long landmarkId,
            @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseEntity.ok(favoriteService.removeLandmarkFromFavorites(jwt, landmarkId));
    }

    @GetMapping("/landmarks")
    public ResponseEntity<ApiResponse<List<LandmarkCardResponse>>> getFavoriteLandmarks(
            @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseEntity.ok(favoriteService.getFavoriteLandmarks(jwt));
    }
}