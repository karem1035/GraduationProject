package com.example.Toda.controller;

import com.example.Toda.DTO.ApiResponse;
import com.example.Toda.DTO.TouristProfileRequest;
import com.example.Toda.DTO.TouristProfileResponse;
import com.example.Toda.service.TouristProfileService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tourist")
public class TouristProfileController {

    private final TouristProfileService touristProfileService;

    public TouristProfileController(TouristProfileService touristProfileService) {
        this.touristProfileService = touristProfileService;
    }

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<TouristProfileResponse>> createProfile(@Valid @RequestBody TouristProfileRequest request) {
        TouristProfileResponse response = touristProfileService.createProfile(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Tourist profile created successfully"));
    }

    @PatchMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<TouristProfileResponse>> updateProfile(
            @PathVariable Long id,
            @RequestBody TouristProfileRequest request) {
        TouristProfileResponse response = touristProfileService.updateProfile(id, request);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(response, "Tourist profile updated successfully"));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<TouristProfileResponse>> getProfile(@PathVariable Long id) {
        TouristProfileResponse response = touristProfileService.getProfileById(id);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(response, "Tourist profile retrieved successfully"));
    }

    @GetMapping("/profiles")
    public ResponseEntity<ApiResponse<List<TouristProfileResponse>>> getProfilesByUserId(
            @RequestParam Long userId) {
        List<TouristProfileResponse> responses = touristProfileService.getProfilesByUserId(userId);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(responses, "Tourist profiles retrieved successfully"));
    }

    @GetMapping("/profiles/all")
    public ResponseEntity<ApiResponse<Page<TouristProfileResponse>>> getAllTourists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TouristProfileResponse> responsePage = touristProfileService.getAllTourists(pageable);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(responsePage, "Tourists retrieved successfully"));
    }

    @DeleteMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(@PathVariable Long id) {
        touristProfileService.deleteProfile(id);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(null, "Tourist profile deleted successfully"));
    }
}
