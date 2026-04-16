package com.example.Toda.service;

import com.example.Toda.DTO.TripBasicInfoRequest;
import com.example.Toda.DTO.TripCardResponse;
import com.example.Toda.DTO.TripInfoPriceRequest;
import com.example.Toda.DTO.TripInfoTimeRequest;
import com.example.Toda.Entity.Trip;
import com.example.Toda.Entity.TripStatus;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.mapper.TripMapper;
import com.example.Toda.repo.TripRepository;
import com.example.Toda.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class TripService {
    private  final TripRepository tripRepository;
    @Autowired
    private TripMapper tripMapper;
    private final UserRepo userRepo;
    private final String uploadDir = "uploads/covers/";
    public TripService(TripRepository tripRepository, UserRepo userRepo) {
        this.tripRepository = tripRepository;
        this.userRepo = userRepo;
    }
    public void createCustomTrip(TripBasicInfoRequest request, String username){

        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTourGuide() == null) {
            throw new RuntimeException("This user is not registered as a Tour Guide");
        }

        Trip trip = tripMapper.toEntity(request);
        trip.setTourGuide(user.getTourGuide());
        tripRepository.save(trip);

    }

    public void addTripTime(TripInfoTimeRequest request, String username) {
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTourGuide() == null) {
            throw new RuntimeException("This user is not registered as a Tour Guide");
        }
        Trip trip=tripMapper.toTrip(request);
        trip.setTourGuide(user.getTourGuide());
        tripRepository.save(trip);

    }

    public void addTripPrice(TripInfoPriceRequest request, String username) {
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTourGuide() == null) {
            throw new RuntimeException("This user is not registered as a Tour Guide");
        }
        Trip trip=tripMapper.toTrip(request);
        trip.setTourGuide(user.getTourGuide());
        tripRepository.save(trip);
    }

    public String saveTripCover(Long tripId, MultipartFile file) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            trip.setTripCoverImage(path.toString());
            tripRepository.save(trip);

            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    public List<TripCardResponse> getFilteredTrips(String email, String status) {
        List<Trip> trips = tripRepository.findByEmailAndOptionalStatus(email, TripStatus.valueOf(status));
        return tripMapper.toCardResponseList(trips);
    }
}
