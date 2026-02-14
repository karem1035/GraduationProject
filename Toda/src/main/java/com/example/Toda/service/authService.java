package com.example.Toda.service;

import com.example.Toda.DTO.RegisterRequest;
import com.example.Toda.DTO.authResponse;
import com.example.Toda.Entity.*;
import com.example.Toda.exception.UserAlreadyExistsException;
import com.example.Toda.repo.TourGuideRepo;
import com.example.Toda.repo.TouristProfileRepo;
import com.example.Toda.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class authService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final TourGuideRepo tourGuideRepo;
    private final TouristProfileRepo touristProfileRepo;
    //@Autowired
  //  private OtpProducer otpProducer;
    Map<String, Object> claims = new HashMap<>();

    public authService(UserRepo userRepo, PasswordEncoder passwordEncoder, JWTService jwtService, TourGuideRepo tourGuideRepo, TouristProfileRepo touristProfileRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tourGuideRepo = tourGuideRepo;
        this.touristProfileRepo = touristProfileRepo;
    }
    @Transactional

    public authResponse signUp(RegisterRequest registerRequest) {
        if (userRepo.findByEmail(registerRequest.email()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(registerRequest.username());
        newUser.setEmail(registerRequest.email());
        newUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        Role role = Role.valueOf(registerRequest.role().toUpperCase());
        newUser.setRole(role);
        UserEntity savedUser =userRepo.save(newUser);
        if(savedUser.getRole().equals(Role.TOURGUIDE)){
            TourGuideEntity tourGuide = new TourGuideEntity();
            tourGuide.setUser(savedUser);
            tourGuide.setName(savedUser.getUsername());
            tourGuide.setEmail(savedUser.getEmail());
            tourGuideRepo.save(tourGuide);
        }else {
            TouristProfileEntity touristProfile = new TouristProfileEntity();
            touristProfile.setUser(savedUser);
            touristProfile.setName(savedUser.getUsername());
            touristProfile.setEmail(savedUser.getEmail());
            touristProfileRepo.save(touristProfile);
        }
        UserPrincipal userPrincipal = new UserPrincipal(newUser);
        String token = jwtService.generateToken(claims, userPrincipal);

        return new authResponse(token);

    }

}

