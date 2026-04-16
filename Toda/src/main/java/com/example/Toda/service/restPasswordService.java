package com.example.Toda.service;

import com.example.Toda.Entity.UserEntity;
import com.example.Toda.exception.UserNotFoundException;
import com.example.Toda.repo.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class restPasswordService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final OTPService otpService; // ضفنا الـ OTPService هنا

    // تحديث الـ Constructor
    public restPasswordService(UserRepo userRepo,
                               BCryptPasswordEncoder bCryptPasswordEncoder,
                               OTPService otpService) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.otpService = otpService;
    }

    @Transactional
    public  void ResetPasswordService(String email, String newPassword, String otp) {


        otpService.verify(otp);

        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));

        userRepo.save(user);
    }
    public void ResetPassword(String email, String newPassword) {

        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepo.save(user);

    }
}