package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.Entity.TripStatus;
import com.example.Toda.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trip")
@Tag(name = "Trips (Guide)", description = "Endpoints for tour guides to create and manage trips")
@SecurityRequirement(name = "Bearer Authentication")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @Operation(summary = "Create a trip (Step 1: Basic info)",
               description = "Creates a new trip with basic information. Returns the trip ID for subsequent steps.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Trip created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid trip data")
    })
    @PostMapping("/create-basic")
    public ResponseEntity<ApiResponse<TripCreateResponse>> createCustomTrip(
            @Parameter(description = "Basic trip information", required = true) @RequestBody TripBasicInfoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        TripCreateResponse response = tripService.createCustomTrip(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Trip created successfully", response)
        );
    }

    @Operation(summary = "Add trip time (Step 2)",
               description = "Adds time details (start/end dates, duration) to an existing trip")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trip time info added"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trip not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not your trip")
    })
    @PostMapping("/{tripId}/trip-time")
    public ResponseEntity<ApiResponse<String>> addTripTime(
            @Parameter(description = "Trip ID", required = true) @PathVariable Long tripId,
            @Parameter(description = "Time details", required = true) @RequestBody TripInfoTimeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        tripService.addTripTime(tripId, request, userDetails.getUsername());
        return ResponseEntity.ok().body(ApiResponse.success("Trip time info added successfully", null));
    }

    @Operation(summary = "Add trip pricing (Step 3)",
               description = "Adds pricing details (price, currency, discounts) to an existing trip")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pricing info added"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trip not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not your trip")
    })
    @PostMapping("/{tripId}/trip-price")
    public ResponseEntity<ApiResponse<String>> addTripPrice(
            @Parameter(description = "Trip ID", required = true) @PathVariable Long tripId,
            @Parameter(description = "Pricing details", required = true) @RequestBody TripInfoPriceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        tripService.addTripPrice(tripId, request, userDetails.getUsername());
        return ResponseEntity.ok().body(ApiResponse.success("Trip pricing info added successfully", null));
    }

    @Operation(summary = "Upload trip cover image (Step 4)",
               description = "Uploads a cover image for an existing trip. Supported formats: jpg, png, gif, webp")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trip not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid file")
    })
    @PostMapping("/{tripId}/upload-cover")
    public ResponseEntity<ApiResponse<String>> uploadTripCover(
            @Parameter(description = "Trip ID", required = true) @PathVariable Long tripId,
            @Parameter(description = "Cover image file", required = true) @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {

        String imageUrl = tripService.saveTripCover(tripId, file, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully", imageUrl));
    }

    @Operation(summary = "Get guide's trips",
               description = "Retrieves all trips for the authenticated tour guide, optionally filtered by status")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trips retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/guideTrips")
    public ResponseEntity<ApiResponse<List<TripCardResponse>>> getGuideTrips(
            @Parameter(description = "Filter by trip status key",
                       schema = @Schema(allowableValues = {"NEW", "UPCOMING", "COMPLETED", "CANCELLED"},
                                         description = "NEW - Draft trip, UPCOMING - Scheduled trip, COMPLETED - Finished trip, CANCELLED - Cancelled trip"))
            @RequestParam(required = false) String statusKey,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        List<TripCardResponse> trips = tripService.getFilteredTrips(email, statusKey);
        return ResponseEntity.ok(ApiResponse.success("Filtered trips fetched successfully", trips));
    }

    @Operation(summary = "Update trip status",
               description = "Allows a tour guide to update the status of their trip. " +
                       "Valid transitions: NEW → UPCOMING, NEW → CANCELLED, UPCOMING → COMPLETED, UPCOMING → CANCELLED")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Status updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trip not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not your trip")
    })
    @PatchMapping("/{tripId}/status")
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