package com.example.Toda.controller;

import com.example.Toda.DTO.ApiResponse;
import com.example.Toda.DTO.ChangePasswordRequest;
import com.example.Toda.DTO.updateProfileRequest;
import com.example.Toda.service.forgetPasswordService;
import com.example.Toda.service.profileService;
import com.example.Toda.service.restPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Profile Management", description = "Endpoints for managing user profile (update info, change password, delete account)")
@SecurityRequirement(name = "Bearer Authentication")
public class profile {
      private final profileService profileService;

    public profile(profileService profileService) {
        this.profileService = profileService;
    }


    @Operation(summary = "Update user profile", description = "Update the authenticated user's profile information (name, email, phone)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<String>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "Updated profile details", required = true) @RequestBody updateProfileRequest request) {
        String email = userDetails.getUsername();
        profileService.updateProfile(email,request);
        return ResponseEntity.ok().body(ApiResponse.success("profile updated successfully",null));
    }

    @Operation(summary = "Check password", description = "Verify if the provided password matches the current user's password")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password check successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Incorrect password")
    })
    @PostMapping("/check-password")
    public ResponseEntity<ApiResponse<String>> checkPassword(
            @Parameter(description = "Password to verify", example = "MySecurePass123!", required = true) @RequestBody String password,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        profileService.checkPassword(email,password);
        return ResponseEntity.ok().body(ApiResponse.success("password check successfully",null));

    }

    @Operation(summary = "Delete account", description = "Soft-delete the user's account. The account can be recovered within 30 days.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Account deactivated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/delete-account")
    public ResponseEntity<ApiResponse<String>> deleteAccount(@AuthenticationPrincipal UserDetails userDetails) {
        profileService.softDeleteAccount(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Account deactivated. You have 30 days to recover it.", null));
    }


}