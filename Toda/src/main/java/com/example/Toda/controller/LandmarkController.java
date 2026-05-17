package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.service.LandmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/landmarks")
@Tag(name = "Landmarks Management", description = "Endpoints for managing landmarks (Tour Guide only)")
public class LandmarkController {

    private final LandmarkService landmarkService;

    public LandmarkController(LandmarkService landmarkService) {
        this.landmarkService = landmarkService;
    }

    /**
     * Create a new landmark.
     */
    @PostMapping
    @Operation(summary = "Create a new landmark",
               description = "Creates a new landmark with name, city, address, type, and description")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",
                    description = "Landmark created successfully",
                    content = @Content(schema = @Schema(implementation = LandmarkResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Invalid input data")
    })
    public ResponseEntity<ApiResponse<LandmarkResponse>> createLandmark(
            @RequestBody LandmarkCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        LandmarkResponse response = landmarkService.createLandmark(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Landmark created successfully", response)
        );
    }

    /**
     * Upload an image for a landmark.
     */
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload landmark image",
               description = "Uploads an image for the specified landmark")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Image uploaded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "Landmark not found")
    })
    public ResponseEntity<ApiResponse<String>> uploadLandmarkImage(
            @Parameter(description = "Landmark ID", required = true) @PathVariable Long id,
            @Parameter(description = "Image file", required = true) @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {

        String imageUrl = landmarkService.uploadLandmarkImage(id, file);
        return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully", imageUrl));
    }

    /**
     * Update an existing landmark.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a landmark",
               description = "Updates an existing landmark. All fields are optional — only provided fields will be updated.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Landmark updated successfully",
                    content = @Content(schema = @Schema(implementation = LandmarkResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "Landmark not found")
    })
    public ResponseEntity<ApiResponse<LandmarkResponse>> updateLandmark(
            @Parameter(description = "Landmark ID", required = true) @PathVariable Long id,
            @RequestBody LandmarkUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        LandmarkResponse response = landmarkService.updateLandmark(id, request);
        return ResponseEntity.ok(ApiResponse.success("Landmark updated successfully", response));
    }

    /**
     * Delete a landmark.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a landmark",
               description = "Deletes a landmark by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Landmark deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "Landmark not found")
    })
    public ResponseEntity<ApiResponse<String>> deleteLandmark(
            @Parameter(description = "Landmark ID", required = true) @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        landmarkService.deleteLandmark(id);
        return ResponseEntity.ok(ApiResponse.success("Landmark deleted successfully", null));
    }

    /**
     * Attach landmarks to a trip.
     */
    @PostMapping("/trip/{tripId}/attach")
    @Operation(summary = "Attach landmarks to a trip",
               description = "Attaches one or more landmarks to an existing trip by providing a list of landmark IDs")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Landmarks attached to trip successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "Trip or landmark not found")
    })
    public ResponseEntity<ApiResponse<String>> attachLandmarksToTrip(
            @Parameter(description = "Trip ID", required = true) @PathVariable Long tripId,
            @RequestBody TripLandmarkRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        ApiResponse<String> response = landmarkService.attachLandmarksToTrip(tripId, request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * Remove a landmark from a trip.
     */
    @DeleteMapping("/trip/{tripId}/{landmarkId}")
    @Operation(summary = "Remove a landmark from a trip",
               description = "Removes a specific landmark from a trip")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Landmark removed from trip successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404",
                    description = "Trip or landmark not found")
    })
    public ResponseEntity<ApiResponse<String>> removeLandmarkFromTrip(
            @Parameter(description = "Trip ID", required = true) @PathVariable Long tripId,
            @Parameter(description = "Landmark ID", required = true) @PathVariable Long landmarkId,
            @AuthenticationPrincipal UserDetails userDetails) {

        ApiResponse<String> response = landmarkService.removeLandmarkFromTrip(tripId, landmarkId, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}