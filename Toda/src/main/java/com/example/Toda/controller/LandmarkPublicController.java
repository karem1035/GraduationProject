package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.Entity.LandmarkType;
import com.example.Toda.service.LandmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/landmarks")
@Tag(name = "Landmarks Public", description = "Public endpoints for viewing landmarks and filtering trips by landmarks")
public class LandmarkPublicController {

    private final LandmarkService landmarkService;

    public LandmarkPublicController(LandmarkService landmarkService) {
        this.landmarkService = landmarkService;
    }

    /**
     * Get all landmarks with optional filters and pagination.
     */
    @GetMapping
    @Operation(summary = "Get all landmarks",
               description = "Returns a paginated list of landmarks with optional filters for type, city, and name")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Landmarks retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<LandmarkCardResponse>>> getAllLandmarks(
            @Parameter(description = "Landmark type filter",
                       example = "MONUMENT",
                       schema = @Schema(allowableValues = {"MONUMENT", "MUSEUM", "NATURAL", "RELIGIOUS", "HISTORICAL", "ENTERTAINMENT", "RESTAURANT", "SHOPPING", "OTHER"}))
            @RequestParam(required = false) String type,
            @Parameter(description = "City name (partial match)", example = "Giza")
            @RequestParam(required = false) String city,
            @Parameter(description = "Landmark name (partial match)", example = "Pyramids")
            @RequestParam(required = false) String name,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        LandmarkType typeEnum = type != null ? LandmarkType.valueOf(type) : null;
        Page<LandmarkCardResponse> landmarks = landmarkService.getFilteredLandmarks(typeEnum, city, name, page, size);
        return ResponseEntity.ok(ApiResponse.success("Landmarks retrieved successfully", landmarks));
    }

    /**
     * Get a single landmark by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get landmark details",
               description = "Returns full details of a specific landmark")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Landmark details retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "Landmark not found")
    })
    public ResponseEntity<ApiResponse<LandmarkResponse>> getLandmarkDetails(
            @Parameter(description = "Landmark ID", required = true) @PathVariable Long id) {

        LandmarkResponse response = landmarkService.getLandmarkDetails(id);
        return ResponseEntity.ok(ApiResponse.success("Landmark details retrieved successfully", response));
    }

    /**
     * Get all trips that include a specific landmark.
     */
    @GetMapping("/{id}/trips")
    @Operation(summary = "Get trips by landmark",
               description = "Returns a paginated list of published trips that include the specified landmark")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Trips retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "Landmark not found")
    })
    public ResponseEntity<ApiResponse<Page<TripCardResponse>>> getTripsByLandmark(
            @Parameter(description = "Landmark ID", required = true) @PathVariable Long id,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Page<TripCardResponse> trips = landmarkService.getTripsByLandmark(id, page, size);
        return ResponseEntity.ok(ApiResponse.success("Trips retrieved successfully", trips));
    }
}