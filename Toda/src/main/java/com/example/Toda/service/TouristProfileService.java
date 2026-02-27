package com.example.Toda.service;

import com.example.Toda.DTO.*;
import com.example.Toda.Entity.TouristProfileEntity;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.exception.TouristNotFoundException;
import com.example.Toda.repo.TouristProfileRepo;
import com.example.Toda.repo.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TouristProfileService {

    private final TouristProfileRepo touristProfileRepo;
    private final UserRepo userRepo;
    private final JWTService jwtService;

    public TouristProfileService(TouristProfileRepo touristProfileRepo, UserRepo userRepo, JWTService jwtService) {
        this.touristProfileRepo = touristProfileRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    // Step 1: Create Basic Profile
    @Transactional
    public void createBasicProfile(TouristBasicInfoRequest request, String token) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new TouristNotFoundException("User not found"));

        TouristProfileEntity profile = touristProfileRepo.findByUserId(user.getId())
                .orElse(new TouristProfileEntity());

        profile.setUser(user);
        profile.setName(request.name());
        profile.setEmail(request.email());
        profile.setType(TouristProfileEntity.TouristType.valueOf(request.type().toUpperCase()));
        profile.setNationality(request.nationality());
        profile.setMotherLanguage(request.motherLanguage());
        profile.setLanguages(request.languages());

        touristProfileRepo.save(profile);
    }

    // Step 1: Get Basic Profile
    public TouristBasicInfoResponse getBasicProfile(String token) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new TouristNotFoundException("User not found"));

        TouristProfileEntity profile = touristProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found"));

        return new TouristBasicInfoResponse(
                profile.getId(),
                profile.getName(),
                profile.getEmail(),
                profile.getType().name(),
                profile.getNationality(),
                profile.getMotherLanguage(),
                profile.getLanguages()
        );
    }

    // Step 2: Complete Travel Info
    @Transactional
    public void completeTravelInfo(TouristTravelInfoRequest request, String token) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new TouristNotFoundException("User not found"));

        TouristProfileEntity profile = touristProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found"));

        profile.setTravelDateFrom(request.travelDateFrom());
        profile.setTravelDateTo(request.travelDateTo());
        profile.setDestinationCity(request.destinationCity());
        profile.setTripType(request.tripType());
        profile.setNumberOfTravelers(request.numberOfTravelers());

        touristProfileRepo.save(profile);
    }

    // Step 2: Get Travel Info
    public TouristTravelInfoResponse getTravelInfo(String token) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new TouristNotFoundException("User not found"));

        TouristProfileEntity profile = touristProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found"));

        return new TouristTravelInfoResponse(
                profile.getId(),
                profile.getTravelDateFrom(),
                profile.getTravelDateTo(),
                profile.getDestinationCity(),
                profile.getTripType(),
                profile.getNumberOfTravelers()
        );
    }

    // Step 3: Add Travel Interests
    @Transactional
    public void addTravelInterests(TouristInterestsRequest request, String token) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new TouristNotFoundException("User not found"));

        TouristProfileEntity profile = touristProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found"));

        profile.setTravelInterests(request.travelInterests());
        touristProfileRepo.save(profile);
    }

    // Step 3: Get Travel Interests
    public TouristInterestsResponse getTravelInterests(String token) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new TouristNotFoundException("User not found"));

        TouristProfileEntity profile = touristProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found"));

        return new TouristInterestsResponse(
                profile.getId(),
                profile.getTravelInterests()
        );
    }

    // Step 4: Complete Preferences
    @Transactional
    public void completePreferences(TouristPreferencesRequest request, String token) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new TouristNotFoundException("User not found"));

        TouristProfileEntity profile = touristProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found"));

        profile.setSpecialNeeds(request.specialNeeds());
        profile.setTravelPreferences(request.travelPreferences());
        profile.setFoodPreference(request.foodPreference());
        profile.setFoodAllergies(request.foodAllergies());
        profile.setNotes(request.notes());

        touristProfileRepo.save(profile);
    }

    // Step 4: Get Preferences
    public TouristPreferencesResponse getPreferences(String token) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new TouristNotFoundException("User not found"));

        TouristProfileEntity profile = touristProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found"));

        return new TouristPreferencesResponse(
                profile.getId(),
                profile.getSpecialNeeds(),
                profile.getTravelPreferences(),
                profile.getFoodPreference(),
                profile.getFoodAllergies(),
                profile.getNotes()
        );
    }

    // Update profile field by token (for file uploads)
    @Transactional
    public void updateProfileFieldByToken(String token, String fieldName, String value) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new TouristNotFoundException("User not found"));

        TouristProfileEntity profile = touristProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found"));

        switch (fieldName) {
            case "profilePhoto" -> profile.setProfilePhoto(value);
            default -> throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }

        touristProfileRepo.save(profile);
    }

    // Delete profile field by token
    @Transactional
    public void deleteProfileFieldByToken(String token, String fieldName) {
        String username = jwtService.extractUsername(token);
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new TouristNotFoundException("User not found"));

        TouristProfileEntity profile = touristProfileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found"));

        switch (fieldName) {
            case "profilePhoto" -> profile.setProfilePhoto(null);
            default -> throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }

        touristProfileRepo.save(profile);
    }

    // Get complete profile by ID
    public TouristProfileResponse getProfileById(Long id) {
        TouristProfileEntity profile = touristProfileRepo.findById(id)
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found with id: " + id));
        return mapToResponse(profile);
    }

    // Get profiles by user ID
    public List<TouristProfileResponse> getProfilesByUserId(Long userId) {
        List<TouristProfileEntity> profiles = touristProfileRepo.findAllByUserId(userId);
        return profiles.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get all tourists (paginated)
    public Page<TouristProfileResponse> getAllTourists(Pageable pageable) {
        return touristProfileRepo.findAll(pageable)
                .map(this::mapToResponse);
    }

    // Delete profile
    @Transactional
    public void deleteProfile(Long id) {
        TouristProfileEntity profile = touristProfileRepo.findById(id)
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found with id: " + id));
        touristProfileRepo.delete(profile);
    }

    // Map entity to response DTO
    private TouristProfileResponse mapToResponse(TouristProfileEntity entity) {
        return new TouristProfileResponse(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getName(),
                entity.getEmail(),
                entity.getType().name(),
                entity.getNationality(),
                entity.getMotherLanguage(),
                entity.getLanguages(),
                entity.getTravelDateFrom(),
                entity.getTravelDateTo(),
                entity.getDestinationCity(),
                entity.getTripType(),
                entity.getNumberOfTravelers(),
                entity.getTravelInterests(),
                entity.getSpecialNeeds(),
                entity.getTravelPreferences(),
                entity.getFoodPreference(),
                entity.getFoodAllergies(),
                entity.getNotes(),
                entity.getProfilePhoto()
        );
    }
}