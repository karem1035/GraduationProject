package com.example.Toda.service;

import com.example.Toda.DTO.*;
import com.example.Toda.Entity.StaticTrip;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.repo.StaticTripRepository;
import com.example.Toda.repo.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StaticTripService {

    private final StaticTripRepository staticTripRepository;
    private final UserRepo userRepo;

    public StaticTripService(StaticTripRepository staticTripRepository, UserRepo userRepo) {
        this.staticTripRepository = staticTripRepository;
        this.userRepo = userRepo;
    }

    @Transactional
    public StaticTripResponse createStaticTrip(StaticTripCreateRequest request, String username) {
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        StaticTrip trip = new StaticTrip();
        trip.setTitle(request.title());
        trip.setDescription(request.description());
        trip.setCity(request.city());
        trip.setMeetingPoint(request.meetingPoint());
        trip.setStartDate(request.startDate());
        trip.setEndDate(request.endDate());
        trip.setPrice(request.price());
        trip.setDuration(request.duration());
        trip.setGroupSize(request.groupSize());
        trip.setCategories(request.categories() != null ? request.categories() : new java.util.ArrayList<>());
        trip.setInclusions(request.inclusions() != null ? request.inclusions() : new java.util.ArrayList<>());
        trip.setCreatedBy(user);

        StaticTrip saved = staticTripRepository.save(trip);
        return mapToFullResponse(saved);
    }

    @Transactional
    public void uploadTripImage(Long tripId, String username, MultipartFile file) {
        StaticTrip trip = staticTripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Static trip not found"));

        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (trip.getCreatedBy().getId() != user.getId()) {
            throw new RuntimeException("You can only upload images to your own trips");
        }

        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads/covers/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(file.getInputStream(), uploadPath.resolve(filename));
            trip.setImageUrl("/uploads/covers/" + filename);
            staticTripRepository.save(trip);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public Page<StaticTripCardResponse> getAllStaticTrips(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return staticTripRepository.findAllByOrderByStartDateDesc(pageable)
                .map(this::mapToCardResponse);
    }

    public StaticTripResponse getStaticTripById(Long id) {
        StaticTrip trip = staticTripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Static trip not found"));
        return mapToFullResponse(trip);
    }

    private StaticTripCardResponse mapToCardResponse(StaticTrip trip) {
        String creatorName = null;
        if (trip.getCreatedBy() != null && trip.getCreatedBy().getTourGuide() != null) {
            creatorName = trip.getCreatedBy().getTourGuide().getName();
        } else if (trip.getCreatedBy() != null) {
            creatorName = trip.getCreatedBy().getUsername();
        }

        return new StaticTripCardResponse(
                trip.getId(),
                trip.getTitle(),
                trip.getCity(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getPrice(),
                trip.getDuration(),
                trip.getImageUrl(),
                creatorName
        );
    }

    private StaticTripResponse mapToFullResponse(StaticTrip trip) {
        StaticTripResponse.CreatorInfo creator = null;
        if (trip.getCreatedBy() != null) {
            String profilePhoto = null;
            String name = trip.getCreatedBy().getUsername();
            if (trip.getCreatedBy().getTourGuide() != null) {
                profilePhoto = trip.getCreatedBy().getTourGuide().getProfilePhoto();
                name = trip.getCreatedBy().getTourGuide().getName();
            }
            creator = new StaticTripResponse.CreatorInfo(
                    trip.getCreatedBy().getId(),
                    name,
                    profilePhoto
            );
        }

        return new StaticTripResponse(
                trip.getId(),
                trip.getTitle(),
                trip.getDescription(),
                trip.getCity(),
                trip.getMeetingPoint(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getPrice(),
                trip.getDuration(),
                trip.getGroupSize(),
                trip.getImageUrl(),
                trip.getCategories(),
                trip.getInclusions(),
                creator
        );
    }
}