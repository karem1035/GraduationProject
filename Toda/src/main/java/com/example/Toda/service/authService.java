package com.example.Toda.service;

import com.example.Toda.DTO.RegisterRequest;
import com.example.Toda.DTO.TourGuideResponse;
import com.example.Toda.DTO.TouristProfileResponse;
import com.example.Toda.DTO.UserWithProfileResponse;
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
import java.util.Optional;

@Service
public class authService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final TourGuideRepo tourGuideRepo;
    private final TouristProfileRepo touristProfileRepo;
    Map<String, Object> claims = new HashMap<>();

    public authService(UserRepo userRepo, PasswordEncoder passwordEncoder, JWTService jwtService, 
                   TourGuideRepo tourGuideRepo, TouristProfileRepo touristProfileRepo) {
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
        UserPrincipal userPrincipal = new UserPrincipal(newUser);
        String token = jwtService.generateToken(claims, userPrincipal);

        return new authResponse(token);

    }

    public UserWithProfileResponse getUserWithProfile(String email) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserWithProfileResponse.UserData userData = new UserWithProfileResponse.UserData(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );

        UserWithProfileResponse response = new UserWithProfileResponse();
        response.setUser(userData);

        // Fetch profile based on role
        if (user.getRole() == Role.TOURGUIDE) {
            Optional<TourGuideEntity> tourGuide = tourGuideRepo.findByEmail(email);
            if (tourGuide.isPresent()) {
                TourGuideEntity tg = tourGuide.get();
                UserWithProfileResponse.TourGuideProfileData profileData = 
                        new UserWithProfileResponse.TourGuideProfileData(
                                tg.getId(),
                                tg.getName(),
                                tg.getEmail(),
                                tg.getCity(),
                                tg.getPhone(),
                                tg.getLicensedNumber(),
                                tg.getYearsOfExperience(),
                                tg.getGuideType() != null ? tg.getGuideType().name() : null,
                                tg.getTourType() != null ? tg.getTourType().name() : null,
                                tg.getCoveredArea(),
                                tg.getTourDuration(),
                                tg.getLanguages(),
                                tg.getProfilePhoto(),
                                tg.getLicense(),
                                tg.getIdDocument()
                        );
                response.setTourGuideProfile(profileData);
            }
        } else if (user.getRole() == Role.TOURIST) {
            Optional<TouristProfileEntity> tourist = touristProfileRepo.findByUserId(user.getId());
            if (tourist.isPresent()) {
                TouristProfileEntity tp = tourist.get();
                UserWithProfileResponse.TouristProfileData profileData = 
                        new UserWithProfileResponse.TouristProfileData(
                                tp.getId(),
                                tp.getName(),
                                tp.getEmail(),
                                tp.getType() != null ? tp.getType().name() : null,
                                tp.getNationality(),
                                tp.getMotherLanguage(),
                                tp.getLanguages(),
                                tp.getPhone(),
                                tp.getProfilePhoto(),
                                tp.getTravelDateFrom(),
                                tp.getTravelDateTo(),
                                tp.getDestinationCity(),
                                tp.getTripType(),
                                tp.getNumberOfTravelers(),
                                tp.getTravelInterests(),
                                tp.getSpecialNeeds(),
                                tp.getTravelPreferences(),
                                tp.getFoodPreference(),
                                tp.getFoodAllergies(),
                                tp.getNotes()
                        );
                response.setTouristProfile(profileData);
            }
        }

        return response;
    }

}

