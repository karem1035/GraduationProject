package com.example.Toda.service;

import com.example.Toda.DTO.updateProfileRequest;
import com.example.Toda.Entity.TourGuideEntity;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.repo.TourGuideRepo;
import com.example.Toda.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class profileService {
    private final UserRepo userRepo;
    private final TourGuideRepo tourGuideRepo;

    public profileService(UserRepo userRepo, TourGuideRepo tourGuideRepo) {
        this.userRepo = userRepo;
        this.tourGuideRepo = tourGuideRepo;
    }

    @Transactional
    public void updateProfile(String currentEmail, updateProfileRequest request) {

        UserEntity user = userRepo.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fullName = request.firstName() + " " + request.lastName();
        user.setUsername(fullName);
        user.setEmail(request.email());
        TourGuideEntity guide = user.getTourGuide();
        if (guide != null) {
            guide.setName(fullName);
            guide.setPhone(request.phone());
            guide.setEmail(request.email());
        }
    }

    public void changeTourguidePassword(String email) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    public void checkPassword(String email, String password) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getPassword().equals(password)) throw new RuntimeException("Passwords don't match");

    }

    @Transactional
    public void softDeleteAccount(String username) {
        UserEntity user = userRepo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeleted(true);
        user.setDeletionDate(LocalDateTime.now().plusDays(30));
        userRepo.save(user);
    }
}
