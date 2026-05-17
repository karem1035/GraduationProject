package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.service.TouristProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tourist")
@Tag(name = "Tourist Profile", description = "Endpoints for tourist profile management (basic info, travel info, interests, preferences)")
@SecurityRequirement(name = "Bearer Authentication")
public class TouristProfileController {

    private final TouristProfileService touristProfileService;

    @Value("${app.server.base-url}")
    private String serverBaseUrl;

    public TouristProfileController(TouristProfileService touristProfileService) {
        this.touristProfileService = touristProfileService;
    }
    @Operation(summary = "Get tourist signup details", description = "Retrieves the tourist's email and basic signup details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Details retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/profile/SingUpDetails")
    public ResponseEntity<ApiResponse<TourGuideBasicInfoResponse>> getTouristEmailAndPass(@RequestHeader("Authorization") String authHeader)
    {
        if(!authHeader.startsWith("Bearer ")||authHeader==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String token = authHeader.substring(7);
        TourGuideBasicInfoResponse response=touristProfileService.ReturnBasicProfile(token);
        return ResponseEntity.ok().body(ApiResponse.success("success",response));
    }
    // ==================== STEP 1: Basic Information ====================

    @PostMapping("/profile/basic-info")
    @Operation(summary = "Create or update basic tourist information", 
               description = "Step 1 of profile creation. Sets personal information including name, email, type, nationality, and languages")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Basic information created/updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<String>> createBasicProfile(
            @Parameter(description = "Basic information data", required = true)
            @RequestBody TouristBasicInfoRequest basicInfoRequest,
            @Parameter(description = "Bearer token for authentication", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        if (!authHeader.startsWith("Bearer ") || authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid authorization header");
        }
        String token = authHeader.substring(7);
        
        touristProfileService.createBasicProfile(basicInfoRequest, token);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Basic information created successfully", null));
    }

    @GetMapping("/profile/basic-info")
    @Operation(summary = "Get basic tourist information", 
               description = "Retrieves the basic information of the authenticated tourist")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Basic information retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tourist profile not found")
    })
    public ResponseEntity<ApiResponse<TouristBasicInfoResponse>> getBasicProfile(
            @Parameter(description = "Bearer token for authentication", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        if (!authHeader.startsWith("Bearer ") || authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid authorization header");
        }
        String token = authHeader.substring(7);
        
        TouristBasicInfoResponse response = touristProfileService.getBasicProfile(token);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Basic information retrieved successfully", response));
    }

    // ==================== STEP 2: Travel Information ====================

    @PostMapping("/profile/travel-info")
    @Operation(summary = "Create or update travel information", 
               description = "Step 2 of profile creation. Sets travel details including dates, destination, trip type, and number of travelers")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Travel information created/updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tourist profile not found")
    })
    public ResponseEntity<ApiResponse<String>> createTravelInfo(
            @Parameter(description = "Travel information data", required = true)
            @RequestBody TouristTravelInfoRequest travelInfoRequest,
            @Parameter(description = "Bearer token for authentication", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        if (!authHeader.startsWith("Bearer ") || authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid authorization header");
        }
        String token = authHeader.substring(7);
        
        touristProfileService.completeTravelInfo(travelInfoRequest, token);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Travel information created successfully", null));
    }

    @GetMapping("/profile/travel-info")
    @Operation(summary = "Get travel information", 
               description = "Retrieves the travel information of the authenticated tourist")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Travel information retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tourist profile not found")
    })
    public ResponseEntity<ApiResponse<TouristTravelInfoResponse>> getTravelInfo(
            @Parameter(description = "Bearer token for authentication", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        if (!authHeader.startsWith("Bearer ") || authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid authorization header");
        }
        String token = authHeader.substring(7);
        
        TouristTravelInfoResponse response = touristProfileService.getTravelInfo(token);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Travel information retrieved successfully", response));
    }

    // ==================== STEP 3: Travel Interests ====================

    @PostMapping("/profile/interests")
    @Operation(summary = "Create or update travel interests", 
               description = "Step 3 of profile creation. Sets the tourist's travel interests")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Travel interests created/updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tourist profile not found")
    })
    public ResponseEntity<ApiResponse<String>> createInterests(
            @Parameter(description = "Travel interests data", required = true)
            @RequestBody TouristInterestsRequest interestsRequest,
            @Parameter(description = "Bearer token for authentication", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        if (!authHeader.startsWith("Bearer ") || authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid authorization header");
        }
        String token = authHeader.substring(7);
        
        touristProfileService.addTravelInterests(interestsRequest, token);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Travel interests created successfully", null));
    }

    @GetMapping("/profile/interests")
    @Operation(summary = "Get travel interests", 
               description = "Retrieves the travel interests of the authenticated tourist")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Travel interests retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tourist profile not found")
    })
    public ResponseEntity<ApiResponse<TouristInterestsResponse>> getInterests(
            @Parameter(description = "Bearer token for authentication", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        if (!authHeader.startsWith("Bearer ") || authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid authorization header");
        }
        String token = authHeader.substring(7);
        
        TouristInterestsResponse response = touristProfileService.getTravelInterests(token);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Travel interests retrieved successfully", response));
    }

    // ==================== STEP 4: Preferences ====================

    @PostMapping("/profile/preferences")
    @Operation(summary = "Create or update preferences", 
               description = "Step 4 of profile creation. Sets special needs, travel preferences, food preferences, and notes")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Preferences created/updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tourist profile not found")
    })
    public ResponseEntity<ApiResponse<String>> createPreferences(
            @Parameter(description = "Preferences data", required = true)
            @RequestBody TouristPreferencesRequest preferencesRequest,
            @Parameter(description = "Bearer token for authentication", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        if (!authHeader.startsWith("Bearer ") || authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid authorization header");
        }
        String token = authHeader.substring(7);
        
        touristProfileService.completePreferences(preferencesRequest, token);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Preferences created successfully", null));
    }

    @GetMapping("/profile/preferences")
    @Operation(summary = "Get preferences", 
               description = "Retrieves the preferences of the authenticated tourist")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Preferences retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tourist profile not found")
    })
    public ResponseEntity<ApiResponse<TouristPreferencesResponse>> getPreferences(
            @Parameter(description = "Bearer token for authentication", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        if (!authHeader.startsWith("Bearer ") || authHeader == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid authorization header");
        }
        String token = authHeader.substring(7);
        
        TouristPreferencesResponse response = touristProfileService.getPreferences(token);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Preferences retrieved successfully", response));
    }

    // ==================== PROFILE PHOTO UPLOAD ====================

    @PostMapping(value = "/profile/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload profile photo", 
               description = "Upload a profile photo for the authenticated tourist. Supported formats: jpg, png, gif, webp")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Photo uploaded successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid file type or size"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    public ResponseEntity<ApiResponse<String>> uploadProfilePhoto(
            @Parameter(description = "Bearer token for authentication", required = true)
            @RequestHeader("Authorization") String authHeader,
            @Parameter(description = "Profile photo file", required = true, 
                      content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {
        
        String token = authHeader.substring(7);
        String fileUrl = saveFile(file, "profile-photos");
        touristProfileService.updateProfileFieldByToken(token, "profilePhoto", fileUrl);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(fileUrl, "Profile photo uploaded successfully"));
    }

    @DeleteMapping("/profile/photo")
    @Operation(summary = "Delete profile photo", 
               description = "Delete the profile photo of the authenticated tourist")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Photo deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    public ResponseEntity<ApiResponse<String>> deleteProfilePhoto(
            @Parameter(description = "Bearer token for authentication", required = true)
            @RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.substring(7);
        touristProfileService.deleteProfileFieldByToken(token, "profilePhoto");
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(null, "Profile photo deleted successfully"));
    }

    // ==================== ADDITIONAL ENDPOINTS ====================

    @GetMapping("/profile/{id}")
    @Operation(summary = "Get tourist profile by ID", 
               description = "Retrieves a complete tourist profile by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tourist profile not found")
    })
    public ResponseEntity<ApiResponse<TouristProfileResponse>> getProfile(
            @Parameter(description = "Tourist profile ID", required = true)
            @PathVariable Long id) {
        
        TouristProfileResponse response = touristProfileService.getProfileById(id);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Tourist profile retrieved successfully", response));
    }

    @GetMapping("/profiles")
    @Operation(summary = "Get tourist profiles by user ID", 
               description = "Retrieves all tourist profiles for a specific user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profiles retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<TouristProfileResponse>>> getProfilesByUserId(
            @Parameter(description = "User ID", required = true)
            @RequestParam Long userId) {
        
        List<TouristProfileResponse> responses = touristProfileService.getProfilesByUserId(userId);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Tourist profiles retrieved successfully", responses));
    }

    @GetMapping("/profiles/all")
    @Operation(summary = "Get all tourists", 
               description = "Retrieves all tourist profiles with pagination")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tourists retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<TouristProfileResponse>>> getAllTourists(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TouristProfileResponse> responsePage = touristProfileService.getAllTourists(pageable);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Tourists retrieved successfully", responsePage));
    }

    @DeleteMapping("/profile/{id}")
    @Operation(summary = "Delete tourist profile", 
               description = "Deletes a tourist profile by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tourist profile not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteProfile(
            @Parameter(description = "Tourist profile ID", required = true)
            @PathVariable Long id) {
        
        touristProfileService.deleteProfile(id);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Tourist profile deleted successfully", null));
    }

    // ==================== HELPER METHODS ====================

    private String saveFile(MultipartFile file, String folderName) {
        try {
            // Basic Content-Type validation to prevent executable uploads
            String contentType = file.getContentType();
            if (contentType == null || !(contentType.startsWith("image/") || contentType.equals("application/pdf"))) {
                throw new RuntimeException("Invalid file type. Only images and PDFs are allowed.");
            }

            // Create upload directory if it doesn't exist
            String uploadDir = "uploads/" + folderName;
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // Return full file URL with server base URL
            return serverBaseUrl + "/uploads/" + folderName + "/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }
}