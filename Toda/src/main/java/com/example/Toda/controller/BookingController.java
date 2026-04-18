package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.Entity.BookingRequest;
import com.example.Toda.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")

public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ==================== Guide Endpoints ====================

    @GetMapping("/requests/pending")
    public ResponseEntity<ApiResponse<List<BookingRequestResponse>>> getPendingRequests(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<BookingRequestResponse> responses = bookingService.getRequestsByStatus(
                userDetails.getUsername(),
                BookingRequest.RequestStatus.PENDING
        );
        return ResponseEntity.ok(ApiResponse.success("Pending requests retrieved", responses));
    }

    @GetMapping("/requests/accepted")
    public ResponseEntity<ApiResponse<List<BookingRequestResponse>>> getAcceptedRequests(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<BookingRequestResponse> responses = bookingService.getRequestsByStatus(
                userDetails.getUsername(),
                BookingRequest.RequestStatus.ACCEPTED
        );
        return ResponseEntity.ok(ApiResponse.success("Accepted requests retrieved", responses));
    }

    @PatchMapping("/requests/{requestId}/accept")
    public ResponseEntity<ApiResponse<String>> acceptRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal UserDetails userDetails) {

        bookingService.updateStatus(requestId, BookingRequest.RequestStatus.ACCEPTED, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Trip successfully confirmed", null));
    }

    @PatchMapping("/requests/{requestId}/decline")
    public ResponseEntity<ApiResponse<String>> declineRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal UserDetails userDetails) {

        bookingService.updateStatus(requestId, BookingRequest.RequestStatus.DECLINED, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Trip request declined successfully", null));
    }

    // ==================== Tourist Endpoints ====================

    /**
     * Create a booking request for a specific trip.
     */
    @PostMapping("/trips/{tripId}/book")
    public ResponseEntity<ApiResponse<TouristBookingResponse>> createBooking(
            @PathVariable Long tripId,
            @RequestBody BookingCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Override tripId from path variable for consistency
        BookingCreateRequest updatedRequest = new BookingCreateRequest(
                tripId,
                request.category(),
                request.date(),
                request.touristCount(),
                request.notes()
        );

        TouristBookingResponse response = bookingService.createBookingRequest(updatedRequest, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Booking request created successfully", response)
        );
    }

    /**
     * Get all booking requests for the authenticated tourist.
     */
    @GetMapping("/tourist/bookings")
    public ResponseEntity<ApiResponse<List<TouristBookingResponse>>> getTouristBookings(
            @RequestParam(required = false) String status,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<TouristBookingResponse> responses;

        if (status != null) {
            responses = bookingService.getTouristBookingsByStatus(
                    userDetails.getUsername(),
                    BookingRequest.RequestStatus.valueOf(status.toUpperCase())
            );
        } else {
            responses = bookingService.getTouristBookings(userDetails.getUsername());
        }

        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", responses));
    }

    /**
     * Cancel a pending booking request.
     */
    @PatchMapping("/tourist/bookings/{requestId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelBooking(
            @PathVariable Long requestId,
            @AuthenticationPrincipal UserDetails userDetails) {

        bookingService.cancelBookingRequest(requestId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Booking request cancelled successfully", null));
    }
}