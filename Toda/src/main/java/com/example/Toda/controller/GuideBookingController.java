package com.example.Toda.controller;

import com.example.Toda.DTO.ApiResponse;
import com.example.Toda.DTO.GuideBookingCreateRequest;
import com.example.Toda.DTO.GuideBookingResponse;
import com.example.Toda.Entity.GuideBookingRequest;
import com.example.Toda.service.GuideBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guide-bookings")
@Tag(name = "01. Marketplace — Guide Booking Requests", description = "Tourists can request custom bookings from tour guides; guides can accept or reject")
@SecurityRequirement(name = "Bearer Authentication")
public class GuideBookingController {

    private final GuideBookingService guideBookingService;

    public GuideBookingController(GuideBookingService guideBookingService) {
        this.guideBookingService = guideBookingService;
    }

    // ==================== Tourist Endpoints ====================

    @Operation(summary = "Create a guide booking request",
               description = "A tourist sends a custom booking request to a tour guide with optional title, dates, description, and price")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Booking request created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tour guide not found")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<GuideBookingResponse>> createBookingRequest(
            @RequestBody GuideBookingCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        GuideBookingResponse response = guideBookingService.createBookingRequest(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking request created successfully", response));
    }

    @Operation(summary = "Get tourist's guide booking requests",
               description = "Returns all booking requests sent by the authenticated tourist")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking requests retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/tourist")
    public ResponseEntity<ApiResponse<List<GuideBookingResponse>>> getTouristBookings(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<GuideBookingResponse> responses = guideBookingService.getTouristBookings(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Tourist bookings retrieved successfully", responses));
    }

    // ==================== Guide Endpoints ====================

    @Operation(summary = "Get guide's received booking requests",
               description = "Returns all booking requests received by the authenticated tour guide")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking requests retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/guide")
    public ResponseEntity<ApiResponse<List<GuideBookingResponse>>> getGuideBookings(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<GuideBookingResponse> responses = guideBookingService.getGuideBookings(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Guide bookings retrieved successfully", responses));
    }

    @Operation(summary = "Get guide's booking requests by status",
               description = "Returns booking requests received by the authenticated tour guide, filtered by status")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking requests retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/guide/{status}")
    public ResponseEntity<ApiResponse<List<GuideBookingResponse>>> getGuideBookingsByStatus(
            @Parameter(description = "Booking status filter", example = "PENDING", required = true)
            @PathVariable String status,
            @AuthenticationPrincipal UserDetails userDetails) {

        GuideBookingRequest.GuideBookingStatus bookingStatus =
                GuideBookingRequest.GuideBookingStatus.valueOf(status.toUpperCase());

        List<GuideBookingResponse> responses = guideBookingService.getGuideBookingsByStatus(
                userDetails.getUsername(), bookingStatus);
        return ResponseEntity.ok(ApiResponse.success("Guide bookings retrieved successfully", responses));
    }

    @Operation(summary = "Accept a booking request",
               description = "A tour guide accepts a pending booking request")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking request accepted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking request not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Booking cannot be accepted")
    })
    @PatchMapping("/{bookingId}/accept")
    public ResponseEntity<ApiResponse<GuideBookingResponse>> acceptBooking(
            @Parameter(description = "Booking request ID", required = true) @PathVariable Long bookingId,
            @AuthenticationPrincipal UserDetails userDetails) {

        GuideBookingResponse response = guideBookingService.acceptBooking(bookingId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Booking request accepted successfully", response));
    }

    @Operation(summary = "Reject a booking request",
               description = "A tour guide rejects a pending booking request")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking request rejected"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking request not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Booking cannot be rejected")
    })
    @PatchMapping("/{bookingId}/reject")
    public ResponseEntity<ApiResponse<GuideBookingResponse>> rejectBooking(
            @Parameter(description = "Booking request ID", required = true) @PathVariable Long bookingId,
            @AuthenticationPrincipal UserDetails userDetails) {

        GuideBookingResponse response = guideBookingService.rejectBooking(bookingId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Booking request rejected successfully", response));
    }
}