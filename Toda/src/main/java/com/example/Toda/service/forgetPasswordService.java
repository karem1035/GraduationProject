package com.example.Toda.service;

import com.example.Toda.Entity.OtpEntity;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.exception.UserNotFoundException;
import com.example.Toda.repo.OTPRepo;
import com.example.Toda.repo.UserRepo;
import com.example.Toda.util.OtpUtils;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class forgetPasswordService {
    private final UserRepo userRepo;
    private final OTPRepo otpRepo;
    private final OtpProducer otpProducer;


    public forgetPasswordService(UserRepo userRepo, OTPRepo otpRepo, OtpProducer otpProducer) {
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
        this.otpProducer = otpProducer;
    }

    public void sendOtp(String email) {
        UserEntity user=userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        String otp=OtpUtils.generateOtp();
        OtpEntity  otpEntity = otpRepo.findByUser_Id(user.getId())
                .orElse(new OtpEntity());
        otpEntity.setUser(user);
        otpEntity.setOtpCode(otp);
        otpEntity.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        otpRepo.save(otpEntity);
       otpProducer.sendOtpMessage(email,otp);
        
    }


}
