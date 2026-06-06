package com.example.Toda.controller;

import com.example.Toda.DTO.ApiResponse;
import com.example.Toda.DTO.TourGuideListItemResponse;
import com.example.Toda.Entity.TourGuideEntity;
import com.example.Toda.repo.TourGuideRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tour-guides")
@Tag(name = "01. Marketplace — Tour Guides", description = "Public endpoint to list all available tour guides")
public class TourGuidePublicController {

    private final TourGuideRepo tourGuideRepo;

    public TourGuidePublicController(TourGuideRepo tourGuideRepo) {
        this.tourGuideRepo = tourGuideRepo;
    }

    @Operation(summary = "List all tour guides",
               description = "Returns a list of all registered tour guides with their basic info. No authentication required.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tour guides retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<TourGuideListItemResponse>>> getAllTourGuides(
            @Parameter(description = "Filter by city", example = "Cairo")
            @RequestParam(required = false) String city) {

        List<TourGuideEntity> guides = tourGuideRepo.findAll();

        // Filter by city if provided
        if (city != null && !city.isBlank()) {
            guides = guides.stream()
                    .filter(g -> g.getCity() != null && g.getCity().toLowerCase().contains(city.toLowerCase()))
                    .collect(Collectors.toList());
        }

        List<TourGuideListItemResponse> response = guides.stream()
                .map(this::mapToListItem)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Tour guides retrieved successfully", response));
    }

    @Operation(summary = "Get a single tour guide",
               description = "Returns basic info for a specific tour guide by ID. No authentication required.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tour guide retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tour guide not found")
    })
    @GetMapping("/{guideId}")
    public ResponseEntity<ApiResponse<TourGuideListItemResponse>> getTourGuideById(
            @Parameter(description = "Tour guide ID", required = true) @PathVariable Long guideId) {

        TourGuideEntity guide = tourGuideRepo.findById(guideId)
                .orElseThrow(() -> new RuntimeException("Tour guide not found"));

        TourGuideListItemResponse response = mapToListItem(guide);
        return ResponseEntity.ok(ApiResponse.success("Tour guide retrieved successfully", response));
    }

    private TourGuideListItemResponse mapToListItem(TourGuideEntity guide) {
        List<String> languages = guide.getLanguages() != null
                ? guide.getLanguages().stream().map(l -> l.getLanguage() + " (" + l.getLevel() + ")").collect(Collectors.toList())
                : List.of();

        return new TourGuideListItemResponse(
                guide.getId(),
                guide.getName(),
                guide.getCity(),
                guide.getYearsOfExperience(),
                guide.getSpecialization(),
                languages,
                guide.getProfilePhoto(),
                guide.getGuideType() != null ? guide.getGuideType().name() : null,
                guide.getTourType() != null ? guide.getTourType().name() : null
        );
    }
}