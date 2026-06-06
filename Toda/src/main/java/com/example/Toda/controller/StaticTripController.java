package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.service.StaticTripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/static-trips")
@Tag(name = "01. Marketplace — Static Trips", description = "Static trips created by any user, viewable by everyone, bookable by tourists")
public class StaticTripController {

    private final StaticTripService staticTripService;

    public StaticTripController(StaticTripService staticTripService) {
        this.staticTripService = staticTripService;
    }

    // ==================== Authenticated Endpoints ====================

    @Operation(summary = "Create a static trip",
               description = "Any authenticated user can create a static trip. All fields except title are optional.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Static trip created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<ApiResponse<StaticTripResponse>> createStaticTrip(
            @RequestBody StaticTripCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        StaticTripResponse response = staticTripService.createStaticTrip(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Static trip created successfully", response));
    }

    @Operation(summary = "Upload static trip image",
               description = "Upload a cover image for a static trip. Only the creator can upload.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Static trip not found")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/{tripId}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadTripImage(
            @Parameter(description = "Static trip ID", required = true) @PathVariable Long tripId,
            @Parameter(description = "Image file") @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {

        staticTripService.uploadTripImage(tripId, userDetails.getUsername(), file);
        return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully", null));
    }

    // ==================== Public Endpoints ====================

    @Operation(summary = "Get all static trips",
               description = "Returns a paginated list of all static trips. No authentication required.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Static trips retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<Page<StaticTripCardResponse>>> getAllStaticTrips(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Page<StaticTripCardResponse> trips = staticTripService.getAllStaticTrips(page, size);
        return ResponseEntity.ok(ApiResponse.success("Static trips retrieved successfully", trips));
    }

    @Operation(summary = "Get static trip details",
               description = "Returns full details of a specific static trip. No authentication required.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Static trip details retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Static trip not found")
    })
    @GetMapping("/{tripId}")
    public ResponseEntity<ApiResponse<StaticTripResponse>> getStaticTripDetails(
            @Parameter(description = "Static trip ID", required = true) @PathVariable Long tripId) {

        StaticTripResponse response = staticTripService.getStaticTripById(tripId);
        return ResponseEntity.ok(ApiResponse.success("Static trip details retrieved successfully", response));
    }
}