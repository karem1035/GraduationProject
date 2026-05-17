package com.example.Toda.controller;

import com.example.Toda.DTO.HomeDashboardResponse;
import com.example.Toda.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@Tag(name = "Home Dashboard", description = "Endpoints for the user home dashboard")
@SecurityRequirement(name = "Bearer Authentication")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Operation(summary = "Get home dashboard", description = "Retrieves dashboard data for the authenticated user including trips, bookings, and statistics")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/dashboard")
    public ResponseEntity<HomeDashboardResponse> getDashboard(@AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        return ResponseEntity.ok(homeService.getDashboardDataByEmail(email));
    }
}