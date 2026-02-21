package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.service.TourGuideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/tourguide")
public class TourGuideController {

    private final TourGuideService tourGuideService;

    public TourGuideController(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
    }

     @PostMapping("/profile/basic-info")
     public ResponseEntity<ApiResponse<String>>createBasicProfile
             (@RequestBody TourGuideBasicInfoRequest basicInfoRequest,@RequestHeader("Authorization")String authHeader)
     {
         if(!authHeader.startsWith("Bearer ")||authHeader==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
         String token = authHeader.substring(7);
      tourGuideService.createBasicprofile(basicInfoRequest,token);
      return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("created",null));

     }
    @GetMapping("/profile/basic-info")
    public ResponseEntity<ApiResponse<TourGuideBasicInfoResponse>> ReturnBasicProfile
            (@RequestHeader("Authorization")String authHeader)
    {
        if(!authHeader.startsWith("Bearer ")||authHeader==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String token = authHeader.substring(7);
        TourGuideBasicInfoResponse response=tourGuideService.ReturnBasicProfile(token);
        return ResponseEntity.ok().body(ApiResponse.success("success",response));

    }
    @PostMapping("/profile/professional-info")
    public ResponseEntity<ApiResponse<String>> profileProfessionalInfo(@RequestHeader("Authorization")String authHeader
       ,@RequestBody TourGuideProfessionalInfoRequest basicInfoRequest)
    {
        if(!authHeader.startsWith("Bearer ")||authHeader==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String token = authHeader.substring(7);
        tourGuideService.compeletProfInfo(basicInfoRequest,token);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("created",null));


    }
    @PostMapping("/profile/languages")
    public ResponseEntity<ApiResponse<String>>createLanguages(@RequestHeader("Authorization")String authHeader
    ,@RequestBody TourGuideLanguagesInfoRequest basicInfoRequest)
    {
        if(!authHeader.startsWith("Bearer ")||authHeader==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String token = authHeader.substring(7);
        tourGuideService.addLanguages(token,basicInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("created",null));
    }
    @PostMapping("/profile/tour-details")
    public ResponseEntity<ApiResponse<String>>createTourDetails(@RequestHeader("Authorization")String authHeader
            ,@RequestBody TourGuideDetailsInfoRequest basicInfoRequest)
    {
        if(!authHeader.startsWith("Bearer ")||authHeader==null)throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String token = authHeader.substring(7);
        tourGuideService.createTourDetails(token,basicInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("created",null));
    }


    @PatchMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<TourGuideResponse>> updateProfile(
            @PathVariable Long id,
            @RequestBody TourGuideRequest request) {
        TourGuideResponse response = tourGuideService.updateProfile(id, request);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success( "Tour guide profile updated successfully" ,response));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ApiResponse<TourGuideResponse>> getProfile(@PathVariable Long id) {
        TourGuideResponse response = tourGuideService.getProfileById(id);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success("Tour guide profile retrieved successfully", response));
    }

    @GetMapping("/profiles")
    public ResponseEntity<ApiResponse<Page<TourGuideResponse>>> getAllTourGuides(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TourGuideResponse> responsePage = tourGuideService.getAllTourGuides(pageable);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success( "Tour guides retrieved successfully",responsePage));
    }

    @PostMapping("/profile/photo")
    public ResponseEntity<ApiResponse<String>> uploadProfilePhoto(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file) {
        String token = authHeader.substring(7);
        String fileUrl = saveFile(file, "profile-photos");
        tourGuideService.updateProfileFieldByToken(token, "profilePhoto", fileUrl);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(fileUrl, "Profile photo uploaded successfully"));
    }

    @DeleteMapping("/profile/photo")
    public ResponseEntity<ApiResponse<String>> deleteProfilePhoto(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        tourGuideService.deleteProfileFieldByToken(token, "profilePhoto");
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(null, "Profile photo deleted successfully"));
    }

    @PostMapping("/profile/license")
    public ResponseEntity<ApiResponse<String>> uploadLicense(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file) {
        String token = authHeader.substring(7);
        String fileUrl = saveFile(file, "licenses");
        tourGuideService.updateProfileFieldByToken(token, "license", fileUrl);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(fileUrl, "License uploaded successfully"));
    }

    @DeleteMapping("/profile/license")
    public ResponseEntity<ApiResponse<String>> deleteLicense(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        tourGuideService.deleteProfileFieldByToken(token, "license");
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(null, "License deleted successfully"));
    }

    @PostMapping("/profile/id")
    public ResponseEntity<ApiResponse<String>> uploadIdDocument(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file) {
        String token = authHeader.substring(7);
        String fileUrl = saveFile(file, "id-documents");
        tourGuideService.updateProfileFieldByToken(token, "idDocument", fileUrl);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(fileUrl, "ID document uploaded successfully"));
    }

    @DeleteMapping("/profile/id")
    public ResponseEntity<ApiResponse<String>> deleteIdDocument(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        tourGuideService.deleteProfileFieldByToken(token, "idDocument");
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(null, "ID document deleted successfully"));
    }

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

            // Return file URL (relative path)
            return "/uploads/" + folderName + "/" + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }
}
