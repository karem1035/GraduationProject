package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.service.TripService;
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

    @PostMapping("/create-basic")
    public ResponseEntity<ApiResponse<String>>createCustomTrip(@RequestBody TripBasicInfoRequest request,@AuthenticationPrincipal UserDetails userDetails){

        tripService.createCustomTrip(request,userDetails.getUsername());
        return ResponseEntity.ok().body(ApiResponse.success("Created",null));



    }
    @PostMapping("/trip-time")
    public ResponseEntity<ApiResponse<String>>CustomTripTime(@RequestBody TripInfoTimeRequest request, @AuthenticationPrincipal UserDetails userDetails){

        tripService.addTripTime(request,userDetails.getUsername());
        return ResponseEntity.ok().body(ApiResponse.success("Added ",null));

    }
    @PostMapping("/trip-price")
    public ResponseEntity<ApiResponse<String>>CustomTripPrice(@RequestBody TripInfoPriceRequest request, @AuthenticationPrincipal UserDetails userDetails){

        tripService.addTripPrice(request,userDetails.getUsername());
        return ResponseEntity.ok().body(ApiResponse.success("Added ",null));

    }
    @PostMapping("/{tripId}/upload-cover")
    public ResponseEntity<ApiResponse<String>>uploadTripCover(@PathVariable Long tripId,
                                                              @RequestParam("file") MultipartFile file)
    {
        String imageUrl = tripService.saveTripCover(tripId, file);
        return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully", imageUrl));
    }
    @GetMapping("/guideTrips")
    public ResponseEntity<ApiResponse<List<TripCardResponse>>> getGuideTrips(
            @RequestParam(required = false) String statusKey,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        List<TripCardResponse> trips = tripService.getFilteredTrips(email, statusKey);
        return ResponseEntity.ok(ApiResponse.success("Filtered trips fetched successfully", trips));
    }

}
