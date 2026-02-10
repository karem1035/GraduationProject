package com.example.Toda.service;

import com.example.Toda.DTO.TourGuideRequest;
import com.example.Toda.DTO.TourGuideResponse;
import com.example.Toda.Entity.TourGuideEntity;
import com.example.Toda.exception.TourGuideNotFoundException;
import com.example.Toda.repo.TourGuideRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TourGuideService {

    private final TourGuideRepo tourGuideRepo;

    public TourGuideService(TourGuideRepo tourGuideRepo) {
        this.tourGuideRepo = tourGuideRepo;
    }

    public TourGuideResponse createProfile(TourGuideRequest request) {
        // Check if email already exists
        if (tourGuideRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Tour guide with this email already exists");
        }

        TourGuideEntity tourGuide = mapToEntity(request);
        TourGuideEntity savedTourGuide = tourGuideRepo.save(tourGuide);
        return mapToResponse(savedTourGuide);
    }

    public TourGuideResponse updateProfile(Long id, TourGuideRequest request) {
        TourGuideEntity existingTourGuide = tourGuideRepo.findById(id)
                .orElseThrow(() -> new TourGuideNotFoundException("Tour guide not found with id: " + id));

        // Check if email is being changed and if it already exists for another guide
        if (!existingTourGuide.getEmail().equals(request.getEmail()) &&
            tourGuideRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Tour guide with this email already exists");
        }

        // Update fields
        updateEntityFromRequest(existingTourGuide, request);
        TourGuideEntity updatedTourGuide = tourGuideRepo.save(existingTourGuide);
        return mapToResponse(updatedTourGuide);
    }

    public TourGuideResponse getProfileById(Long id) {
        TourGuideEntity tourGuide = tourGuideRepo.findById(id)
                .orElseThrow(() -> new TourGuideNotFoundException("Tour guide not found with id: " + id));
        return mapToResponse(tourGuide);
    }

    public Page<TourGuideResponse> getAllTourGuides(Pageable pageable) {
        return tourGuideRepo.findAll(pageable)
                .map(this::mapToResponse);
    }

    public void updateProfileField(Long id, String fieldName, String value) {
        TourGuideEntity tourGuide = tourGuideRepo.findById(id)
                .orElseThrow(() -> new TourGuideNotFoundException("Tour guide not found with id: " + id));

        switch (fieldName) {
            case "profilePhoto":
                tourGuide.setProfilePhoto(value);
                break;
            case "license":
                tourGuide.setLicense(value);
                break;
            case "idDocument":
                tourGuide.setIdDocument(value);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }

        tourGuideRepo.save(tourGuide);
    }

    private TourGuideEntity mapToEntity(TourGuideRequest request) {
        TourGuideEntity entity = new TourGuideEntity();
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setType(TourGuideEntity.GuideType.valueOf(request.getType().toUpperCase()));
        entity.setPhone(request.getPhone());
        entity.setCity(request.getCity());
        entity.setGuideType(TourGuideEntity.GuideTypeCategory.valueOf(request.getGuideType().toUpperCase()));
        entity.setLicensedNumber(request.getLicensedNumber());
        entity.setYearsOfExperience(request.getYearsOfExperience());
        entity.setSpecialization(request.getSpecialization());
        entity.setLanguages(request.getLanguages());
        entity.setTourType(TourGuideEntity.TourType.valueOf(request.getTourType().toUpperCase()));
        entity.setCoveredArea(request.getCoveredArea());
        entity.setPriceRange(request.getPriceRange());
        entity.setTourDuration(request.getTourDuration());
        entity.setProfilePhoto(request.getProfilePhoto());
        entity.setLicense(request.getLicense());
        entity.setIdDocument(request.getIdDocument());
        return entity;
    }

    private void updateEntityFromRequest(TourGuideEntity entity, TourGuideRequest request) {
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getEmail() != null) entity.setEmail(request.getEmail());
        if (request.getType() != null) entity.setType(TourGuideEntity.GuideType.valueOf(request.getType().toUpperCase()));
        if (request.getPhone() != null) entity.setPhone(request.getPhone());
        if (request.getCity() != null) entity.setCity(request.getCity());
        if (request.getGuideType() != null) entity.setGuideType(TourGuideEntity.GuideTypeCategory.valueOf(request.getGuideType().toUpperCase()));
        if (request.getLicensedNumber() != null) entity.setLicensedNumber(request.getLicensedNumber());
        if (request.getYearsOfExperience() != null) entity.setYearsOfExperience(request.getYearsOfExperience());
        if (request.getSpecialization() != null) entity.setSpecialization(request.getSpecialization());
        if (request.getLanguages() != null) entity.setLanguages(request.getLanguages());
        if (request.getTourType() != null) entity.setTourType(TourGuideEntity.TourType.valueOf(request.getTourType().toUpperCase()));
        if (request.getCoveredArea() != null) entity.setCoveredArea(request.getCoveredArea());
        if (request.getPriceRange() != null) entity.setPriceRange(request.getPriceRange());
        if (request.getTourDuration() != null) entity.setTourDuration(request.getTourDuration());
        if (request.getProfilePhoto() != null) entity.setProfilePhoto(request.getProfilePhoto());
        if (request.getLicense() != null) entity.setLicense(request.getLicense());
        if (request.getIdDocument() != null) entity.setIdDocument(request.getIdDocument());
    }

    private TourGuideResponse mapToResponse(TourGuideEntity entity) {
        TourGuideResponse response = new TourGuideResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setEmail(entity.getEmail());
        response.setType(entity.getType().name());
        response.setPhone(entity.getPhone());
        response.setCity(entity.getCity());
        response.setGuideType(entity.getGuideType().name());
        response.setLicensedNumber(entity.getLicensedNumber());
        response.setYearsOfExperience(entity.getYearsOfExperience());
        response.setSpecialization(entity.getSpecialization());
        response.setLanguages(entity.getLanguages());
        response.setTourType(entity.getTourType().name());
        response.setCoveredArea(entity.getCoveredArea());
        response.setPriceRange(entity.getPriceRange());
        response.setTourDuration(entity.getTourDuration());
        response.setProfilePhoto(entity.getProfilePhoto());
        response.setLicense(entity.getLicense());
        response.setIdDocument(entity.getIdDocument());
        return response;
    }
}
