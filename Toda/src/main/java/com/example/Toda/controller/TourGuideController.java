package com.example.Toda.controller;

import com.example.Toda.DTO.ApiResponse;
import com.example.Toda.DTO.TourGuideRequest;
import com.example.Toda.DTO.TourGuideResponse;
import com.example.Toda.service.TourGuideService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tourguide")
public class TourGuideController {

    private final TourGuideService tourGuideService;

    public TourGuideController(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
    }

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<TourGuideResponse>> createProfile(@Valid @RequestBody TourGuideRequest request) {
        TourGuideResponse response = tourGuideService.createProfile(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Tour guide profile created successfully"));
    }

    @PatchMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<TourGuideResponse>> updateProfile(
            @PathVariable Long id,
            @RequestBody TourGuideRequest request) {
        TourGuideResponse response = tourGuideService.updateProfile(id, request);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(response, "Tour guide profile updated successfully"));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<TourGuideResponse>> getProfile(@PathVariable Long id) {
        TourGuideResponse response = tourGuideService.getProfileById(id);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(response, "Tour guide profile retrieved successfully"));
    }

    @GetMapping("/profiles")
    public ResponseEntity<ApiResponse<Page<TourGuideResponse>>> getAllTourGuides(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TourGuideResponse> responsePage = tourGuideService.getAllTourGuides(pageable);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(responsePage, "Tour guides retrieved successfully"));
    }

    @PostMapping("/profile/{id}/photo")
    public ResponseEntity<ApiResponse<String>> uploadProfilePhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String fileUrl = saveFile(file, "profile-photos");
        tourGuideService.updateProfileField(id, "profilePhoto", fileUrl);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(fileUrl, "Profile photo uploaded successfully"));
    }

    @PostMapping("/profile/{id}/license")
    public ResponseEntity<ApiResponse<String>> uploadLicense(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String fileUrl = saveFile(file, "licenses");
        tourGuideService.updateProfileField(id, "license", fileUrl);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(fileUrl, "License uploaded successfully"));
    }

    @PostMapping("/profile/{id}/id")
    public ResponseEntity<ApiResponse<String>> uploadIdDocument(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String fileUrl = saveFile(file, "id-documents");
        tourGuideService.updateProfileField(id, "idDocument", fileUrl);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(fileUrl, "ID document uploaded successfully"));
    }

    private String saveFile(MultipartFile file, String folderName) {
        try {
            // Create upload directory if it doesn't exist
            String uploadDir = "uploads/" + folderName;
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // Return file URL (relative path)
            return "/uploads/" + folderName + "/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }
}
