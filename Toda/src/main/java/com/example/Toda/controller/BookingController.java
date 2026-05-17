package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.Entity.BookingRequest;
import com.example.Toda.service.BookingService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Bookings", description = "Endpoints for managing trip booking requests (Tour Guides accept/decline, Tourists create/cancel)")
@SecurityRequirement(name = "Bearer Authentication")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ==================== Guide Endpoints ====================

    @Operation(summary = "Get pending booking requests",
               description = "Retrieves all pending booking requests for the authenticated tour guide")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Pending requests retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/requests/pending")
    public ResponseEntity<ApiResponse<List<BookingRequestResponse>>> getPendingRequests(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<BookingRequestResponse> responses = bookingService.getRequestsByStatus(
                userDetails.getUsername(),
                BookingRequest.RequestStatus.PENDING
        );
        return ResponseEntity.ok(ApiResponse.success("Pending requests retrieved", responses));
    }

    @Operation(summary = "Get accepted booking requests",
               description = "Retrieves all accepted booking requests for the authenticated tour guide")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Accepted requests retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/requests/accepted")
    public ResponseEntity<ApiResponse<List<BookingRequestResponse>>> getAcceptedRequests(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<BookingRequestResponse> responses = bookingService.getRequestsByStatus(
                userDetails.getUsername(),
                BookingRequest.RequestStatus.ACCEPTED
        );
        return ResponseEntity.ok(ApiResponse.success("Accepted requests retrieved", responses));
    }

    @Operation(summary = "Accept a booking request",
               description = "Allows a tour guide to accept a pending booking request")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking request accepted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking request not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not authorized to manage this request")
    })
    @PatchMapping("/requests/{requestId}/accept")
    public ResponseEntity<ApiResponse<String>> acceptRequest(
            @Parameter(description = "ID of the booking request to accept", example = "1", required = true) @PathVariable Long requestId,
            @AuthenticationPrincipal UserDetails userDetails) {

        bookingService.updateStatus(requestId, BookingRequest.RequestStatus.ACCEPTED, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Trip successfully confirmed", null));
    }

    @Operation(summary = "Decline a booking request",
               description = "Allows a tour guide to decline a pending booking request")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking request declined"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking request not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not authorized to manage this request")
    })
    @PatchMapping("/requests/{requestId}/decline")
    public ResponseEntity<ApiResponse<String>> declineRequest(
            @Parameter(description = "ID of the booking request to decline", example = "1", required = true) @PathVariable Long requestId,
            @AuthenticationPrincipal UserDetails userDetails) {

        bookingService.updateStatus(requestId, BookingRequest.RequestStatus.DECLINED, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Trip request declined successfully", null));
    }

    // ==================== Tourist Endpoints ====================

    @Operation(summary = "Create a booking request",
               description = "Allows a tourist to create a booking request for a specific trip")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Booking request created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Trip not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid booking data")
    })
    @PostMapping("/trips/{tripId}/book")
    public ResponseEntity<ApiResponse<TouristBookingResponse>> createBooking(
            @Parameter(description = "ID of the trip to book", example = "1", required = true) @PathVariable Long tripId,
            @Parameter(description = "Booking details", required = true) @RequestBody BookingCreateRequest request,
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

    @Operation(summary = "Get tourist's bookings",
               description = "Retrieves all booking requests for the authenticated tourist, optionally filtered by status")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Bookings retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/tourist/bookings")
    public ResponseEntity<ApiResponse<List<TouristBookingResponse>>> getTouristBookings(
            @Parameter(description = "Filter by booking status",
                       schema = @Schema(allowableValues = {"PENDING", "ACCEPTED", "DECLINED", "COMPLETED"},
                                         description = "PENDING - Awaiting guide response, ACCEPTED - Guide confirmed, DECLINED - Guide rejected, COMPLETED - Trip finished"))
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

    @Operation(summary = "Cancel a booking request",
               description = "Allows a tourist to cancel their pending booking request")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Booking cancelled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Booking not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Not authorized to cancel this booking")
    })
    @PatchMapping("/tourist/bookings/{requestId}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelBooking(
            @Parameter(description = "ID of the booking request to cancel", example = "1", required = true) @PathVariable Long requestId,
            @AuthenticationPrincipal UserDetails userDetails) {

        bookingService.cancelBookingRequest(requestId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Booking request cancelled successfully", null));
    }
}