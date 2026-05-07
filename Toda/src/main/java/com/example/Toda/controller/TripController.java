package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.Entity.TripStatus;
import com.example.Toda.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trip")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * Step 1: Create a new trip with basic info.
     * Returns the trip ID for subsequent steps.
     */
    @PostMapping("/create-basic")
    public ResponseEntity<ApiResponse<TripCreateResponse>> createCustomTrip(
            @RequestBody TripBasicInfoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        TripCreateResponse response = tripService.createCustomTrip(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Trip created successfully", response)
        );
    }

    /**
     * Step 2: Add time details to an existing trip.
     */
    @PostMapping("/{tripId}/trip-time")
    public ResponseEntity<ApiResponse<String>> addTripTime(
            @PathVariable Long tripId,
            @RequestBody TripInfoTimeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        tripService.addTripTime(tripId, request, userDetails.getUsername());
        return ResponseEntity.ok().body(ApiResponse.success("Trip time info added successfully", null));
    }

    /**
     * Step 3: Add pricing details to an existing trip.
     */
    @PostMapping("/{tripId}/trip-price")
    public ResponseEntity<ApiResponse<String>> addTripPrice(
            @PathVariable Long tripId,
            @RequestBody TripInfoPriceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        tripService.addTripPrice(tripId, request, userDetails.getUsername());
        return ResponseEntity.ok().body(ApiResponse.success("Trip pricing info added successfully", null));
    }

    /**
     * Step 4: Upload a cover image for an existing trip.
     */
    @PostMapping("/{tripId}/upload-cover")
    public ResponseEntity<ApiResponse<String>> uploadTripCover(
            @PathVariable Long tripId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {

        String imageUrl = tripService.saveTripCover(tripId, file, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully", imageUrl));
    }

    /**
     * Get all trips for the authenticated guide, optionally filtered by status.
     */
    @GetMapping("/guideTrips")
    public ResponseEntity<ApiResponse<List<TripCardResponse>>> getGuideTrips(
            @RequestParam(required = false) String statusKey,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        List<TripCardResponse> trips = tripService.getFilteredTrips(email, statusKey);
        return ResponseEntity.ok(ApiResponse.success("Filtered trips fetched successfully", trips));
    }

    /**
     * Update trip status (e.g., NEW → UPCOMING → COMPLETED or CANCELLED).
     * Only the trip owner (guide) can update the status.
     */
    @PatchMapping("/{tripId}/status")
    @Operation(summary = "Update trip status",
               description = "Allows a tour guide to update the status of their trip. " +
                       "Valid transitions: NEW → UPCOMING, NEW → CANCELLED, UPCOMING → COMPLETED, UPCOMING → CANCELLED")
    public ResponseEntity<ApiResponse<TripCardResponse>> updateTripStatus(
            @Parameter(description = "Trip ID", required = true) @PathVariable Long tripId,
            @Parameter(description = "New status (UPCOMING, COMPLETED, CANCELLED)", 
                      required = true,
                      schema = @Schema(allowableValues = {"UPCOMING", "COMPLETED", "CANCELLED"}))
            @RequestParam TripStatus status,
            @AuthenticationPrincipal UserDetails userDetails) {

        TripCardResponse response = tripService.updateTripStatus(tripId, status, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Trip status updated successfully", response));
    }
}
