package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication, registration, and password management")
public class authController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final authService authService;
    private final forgetPasswordService forgetPasswordService;
    private final restPasswordService restPasswordService;
    private final OTPService  otpService;
    HashMap<String, Object> map = new HashMap<>();

    public authController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JWTService jwtService, authService authService, forgetPasswordService forgetPasswordService, restPasswordService restPasswordService, OTPService otpService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;

        this.authService = authService;
        this.forgetPasswordService = forgetPasswordService;
        this.restPasswordService = restPasswordService;
        this.otpService = otpService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate a user with email and password. Returns a JWT token.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<ApiResponse<authResponse>> login(
            @Parameter(description = "Login credentials", required = true) @RequestBody authRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(authRequest.getEmail());

        String Token = jwtService.generateToken(map, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("User logged successfully",new authResponse(Token))
        );

    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user", description = "Create a new user account (Tourist or Tour Guide). Returns a JWT token.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
    })
    public ResponseEntity<ApiResponse<authResponse>> signup(
            @Parameter(description = "Registration details", required = true) @RequestBody RegisterRequest registerRequest) {

      authResponse response= authService.signUp(registerRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(
              ApiResponse.success("User registered successfully",response)
      );

    }

    @PostMapping("/forgetPassword")
    @Operation(summary = "Request password reset", description = "Sends an OTP to the user's email for password reset verification")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OTP sent successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ApiResponse<String>> forgetPassword(
            @Parameter(description = "User email address", example = "john.doe@example.com", required = true)
            @RequestParam String email) {
        forgetPasswordService.sendOtp(email);
        return ResponseEntity.ok().body(ApiResponse.success("OTP has been sent",null));

    }

    @PostMapping("/ResetPassword")
    @Operation(summary = "Reset password", description = "Reset the user's password using email and new password (after OTP verification)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<ApiResponse<String>> ResetPassword(
            @Parameter(description = "Reset password details", required = true) @RequestBody ResetPassword resetPassword) {
        String email=resetPassword.email();
        String newPassword = resetPassword.newPassword();
       restPasswordService.ResetPassword(email,newPassword);
       return ResponseEntity.ok().body(ApiResponse.success("Password has been reset successfully",null));

    }

    @PostMapping("/verify")
    @Operation(summary = "Verify OTP", description = "Verify the OTP code sent to the user's email")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OTP verified successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
    })
    public ResponseEntity<ApiResponse<String>> verifyOTP(
            @Parameter(description = "OTP verification details", required = true) @RequestBody OTP otp) {
        otpService.verify(otp.OTP());
        return ResponseEntity.ok().body(ApiResponse.success("OTP has been verified",null));

    }

    @GetMapping("/me")
    @Operation(summary = "Get current user with profile",
               description = "Returns the current authenticated user along with their profile (TourGuide or Tourist)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token")
    })
    public ResponseEntity<ApiResponse<UserWithProfileResponse>> getMe(
            @Parameter(description = "JWT Bearer token", required = true)
            @RequestHeader("Authorization") String authHeader) {

        // Extract token from Authorization header
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        String email = jwtService.extractUsername(token);

        UserWithProfileResponse response = authService.getUserWithProfile(email);
        return ResponseEntity.ok().body(
                ApiResponse.success("User retrieved successfully", response)
        );
    }

}