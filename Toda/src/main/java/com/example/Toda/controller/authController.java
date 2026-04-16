package com.example.Toda.controller;

import com.example.Toda.DTO.*;
import com.example.Toda.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    public ResponseEntity<ApiResponse<authResponse>> login(@RequestBody authRequest authRequest) {
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
    public ResponseEntity<ApiResponse<authResponse>> signup(@RequestBody RegisterRequest registerRequest) {

      authResponse response= authService.signUp(registerRequest);
      return ResponseEntity.status(HttpStatus.CREATED).body(
              ApiResponse.success("User registered successfully",response)
      );

    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<ApiResponse<String>> forgetPassword(@RequestParam String email) {
        forgetPasswordService.sendOtp(email);
        return ResponseEntity.ok().body(ApiResponse.success("OTP has been sent",null));

    }
    @PostMapping("/ResetPassword")
    public ResponseEntity<ApiResponse<String>> ResetPassword(@RequestBody ResetPassword resetPassword) {
        String email=resetPassword.email();
        String newPassword = resetPassword.newPassword();
       restPasswordService.ResetPassword(email,newPassword);
       return ResponseEntity.ok().body(ApiResponse.success("Password has been reset successfully",null));

    }
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>>verifyOTP(@RequestBody OTP otp) {
        otpService.verify(otp.OTP());
        return ResponseEntity.ok().body(ApiResponse.success("OTP has been verified",null));

    }

    @GetMapping("/me")
    @Operation(summary = "Get current user with profile", 
               description = "Returns the current authenticated user along with their profile (TourGuide or Tourist)")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<UserWithProfileResponse>> getMe(
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

