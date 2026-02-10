package com.example.Toda.service;

import com.example.Toda.DTO.TouristProfileRequest;
import com.example.Toda.DTO.TouristProfileResponse;
import com.example.Toda.Entity.TouristProfileEntity;
import com.example.Toda.exception.TouristNotFoundException;
import com.example.Toda.repo.TouristProfileRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TouristProfileService {

    private final TouristProfileRepo touristProfileRepo;

    public TouristProfileService(TouristProfileRepo touristProfileRepo) {
        this.touristProfileRepo = touristProfileRepo;
    }

    public TouristProfileResponse createProfile(TouristProfileRequest request) {
        TouristProfileEntity profile = mapToEntity(request);
        TouristProfileEntity savedProfile = touristProfileRepo.save(profile);
        return mapToResponse(savedProfile);
    }

    public TouristProfileResponse updateProfile(Long id, TouristProfileRequest request) {
        TouristProfileEntity existingProfile = touristProfileRepo.findById(id)
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found with id: " + id));

        updateEntityFromRequest(existingProfile, request);
        TouristProfileEntity updatedProfile = touristProfileRepo.save(existingProfile);
        return mapToResponse(updatedProfile);
    }

    public TouristProfileResponse getProfileById(Long id) {
        TouristProfileEntity profile = touristProfileRepo.findById(id)
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found with id: " + id));
        return mapToResponse(profile);
    }

    public List<TouristProfileResponse> getProfilesByUserId(Long userId) {
        List<TouristProfileEntity> profiles = touristProfileRepo.findByUserId(userId);
        return profiles.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Page<TouristProfileResponse> getAllTourists(Pageable pageable) {
        return touristProfileRepo.findAll(pageable)
                .map(this::mapToResponse);
    }

    public void deleteProfile(Long id) {
        TouristProfileEntity profile = touristProfileRepo.findById(id)
                .orElseThrow(() -> new TouristNotFoundException("Tourist profile not found with id: " + id));
        touristProfileRepo.delete(profile);
    }

    private TouristProfileEntity mapToEntity(TouristProfileRequest request) {
        TouristProfileEntity entity = new TouristProfileEntity();
        entity.setUserId(request.getUserId());
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setType(TouristProfileEntity.TouristType.valueOf(request.getType().toUpperCase()));
        entity.setNationality(request.getNationality());
        entity.setMotherLanguage(request.getMotherLanguage());
        entity.setLanguages(request.getLanguages());
        entity.setTravelDateFrom(request.getTravelDateFrom());
        entity.setTravelDateTo(request.getTravelDateTo());
        entity.setDestinationCity(request.getDestinationCity());
        entity.setTripType(request.getTripType());
        entity.setNumberOfTravelers(request.getNumberOfTravelers());
        entity.setTravelInterests(request.getTravelInterests());
        entity.setSpecialNeeds(request.getSpecialNeeds());
        entity.setTravelPreferences(request.getTravelPreferences());
        entity.setFoodPreferences(request.getFoodPreferences());
        entity.setNotes(request.getNotes());
        return entity;
    }

    private void updateEntityFromRequest(TouristProfileEntity entity, TouristProfileRequest request) {
        if (request.getUserId() != null) entity.setUserId(request.getUserId());
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getEmail() != null) entity.setEmail(request.getEmail());
        if (request.getType() != null) entity.setType(TouristProfileEntity.TouristType.valueOf(request.getType().toUpperCase()));
        if (request.getNationality() != null) entity.setNationality(request.getNationality());
        if (request.getMotherLanguage() != null) entity.setMotherLanguage(request.getMotherLanguage());
        if (request.getLanguages() != null) entity.setLanguages(request.getLanguages());
        if (request.getTravelDateFrom() != null) entity.setTravelDateFrom(request.getTravelDateFrom());
        if (request.getTravelDateTo() != null) entity.setTravelDateTo(request.getTravelDateTo());
        if (request.getDestinationCity() != null) entity.setDestinationCity(request.getDestinationCity());
        if (request.getTripType() != null) entity.setTripType(request.getTripType());
        if (request.getNumberOfTravelers() != null) entity.setNumberOfTravelers(request.getNumberOfTravelers());
        if (request.getTravelInterests() != null) entity.setTravelInterests(request.getTravelInterests());
        if (request.getSpecialNeeds() != null) entity.setSpecialNeeds(request.getSpecialNeeds());
        if (request.getTravelPreferences() != null) entity.setTravelPreferences(request.getTravelPreferences());
        if (request.getFoodPreferences() != null) entity.setFoodPreferences(request.getFoodPreferences());
        if (request.getNotes() != null) entity.setNotes(request.getNotes());
    }

    private TouristProfileResponse mapToResponse(TouristProfileEntity entity) {
        TouristProfileResponse response = new TouristProfileResponse();
        response.setId(entity.getId());
        response.setUserId(entity.getUserId());
        response.setName(entity.getName());
        response.setEmail(entity.getEmail());
        response.setType(entity.getType().name());
        response.setNationality(entity.getNationality());
        response.setMotherLanguage(entity.getMotherLanguage());
        response.setLanguages(entity.getLanguages());
        response.setTravelDateFrom(entity.getTravelDateFrom());
        response.setTravelDateTo(entity.getTravelDateTo());
        response.setDestinationCity(entity.getDestinationCity());
        response.setTripType(entity.getTripType());
        response.setNumberOfTravelers(entity.getNumberOfTravelers());
        response.setTravelInterests(entity.getTravelInterests());
        response.setSpecialNeeds(entity.getSpecialNeeds());
        response.setTravelPreferences(entity.getTravelPreferences());
        response.setFoodPreferences(entity.getFoodPreferences());
        response.setNotes(entity.getNotes());
        return response;
    }
}
