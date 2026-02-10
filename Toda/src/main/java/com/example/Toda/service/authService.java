package com.example.Toda.service;

import com.example.Toda.DTO.RegisterRequest;
import com.example.Toda.DTO.authResponse;
import com.example.Toda.Entity.OtpEntity;
import com.example.Toda.Entity.Role;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.Entity.UserPrincipal;
import com.example.Toda.exception.UserAlreadyExistsException;
import com.example.Toda.repo.UserRepo;
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
    //@Autowired
  //  private OtpProducer otpProducer;
    Map<String, Object> claims = new HashMap<>();

    public authService(UserRepo userRepo, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public authResponse signUp(RegisterRequest registerRequest) {
        if (userRepo.findByEmail(registerRequest.email()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(registerRequest.username());
        newUser.setEmail(registerRequest.email());
        newUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        newUser.setRole(Role.TOURIST);
        userRepo.save(newUser);
        UserPrincipal userPrincipal = new UserPrincipal(newUser);
        String token = jwtService.generateToken(claims, userPrincipal);

        return new authResponse(token);

    }

}

