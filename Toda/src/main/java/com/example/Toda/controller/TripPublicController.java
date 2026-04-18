package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/trips")
public class TripPublicController {

    private final TripService tripService;

    public TripPublicController(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * Get all published/upcoming trips (paginated).
     * Public endpoint — no authentication required.
     */
    @GetMapping
    @Operation(summary = "Get all published trips",
               description = "Returns a paginated list of all upcoming/published trips")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trips retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<TripCardResponse>>> getAllTrips(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Page<TripCardResponse> trips = tripService.getAllPublishedTrips(page, size);
        return ResponseEntity.ok(ApiResponse.success("Trips retrieved successfully", trips));
    }

    /**
     * Get full details of a specific trip.
     * Public endpoint — no authentication required.
     */
    @GetMapping("/{tripId}")
    @Operation(summary = "Get trip details",
               description = "Returns full details of a specific trip including guide information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Trip details retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trip not found")
    })
    public ResponseEntity<ApiResponse<TripDetailsResponse>> getTripDetails(
            @Parameter(description = "Trip ID", required = true)
            @PathVariable Long tripId) {

        TripDetailsResponse response = tripService.getTripDetails(tripId);
        return ResponseEntity.ok(ApiResponse.success("Trip details retrieved successfully", response));
    }

    /**
     * Search trips with filters.
     * Public endpoint — no authentication required.
     */
    @GetMapping("/search")
    @Operation(summary = "Search trips",
               description = "Search and filter published trips by city, category, dates, price, and group size")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<TripCardResponse>>> searchTrips(
            @Parameter(description = "City name (partial match)")
            @RequestParam(required = false) String city,
            @Parameter(description = "Category (partial match)")
            @RequestParam(required = false) String category,
            @Parameter(description = "Trip start date (from)")
            @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "Trip end date (to)")
            @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "Minimum price per tourist")
            @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Maximum price per tourist")
            @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Number of travelers")
            @RequestParam(required = false) Integer groupSize,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Page<TripCardResponse> results = tripService.searchTrips(
                city, category, startDate, endDate,
                minPrice, maxPrice, groupSize, page, size);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", results));
    }

    /**
     * Get all published trips by a specific guide.
     * Public endpoint — no authentication required.
     */
    @GetMapping("/guide/{guideId}")
    @Operation(summary = "Get trips by guide",
               description = "Returns all published trips by a specific tour guide")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Guide trips retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<TripCardResponse>>> getTripsByGuide(
            @Parameter(description = "Tour Guide ID", required = true)
            @PathVariable Long guideId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Page<TripCardResponse> trips = tripService.getTripsByGuide(guideId, page, size);
        return ResponseEntity.ok(ApiResponse.success("Guide trips retrieved successfully", trips));
    }
}