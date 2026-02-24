package com.example.Toda.service;

import com.example.Toda.DTO.RegisterRequest;
import com.example.Toda.DTO.authResponse;
import com.example.Toda.Entity.*;
import com.example.Toda.exception.UserAlreadyExistsException;
import com.example.Toda.repo.UserRepo;
import jakarta.transaction.Transactional;
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
    Map<String, Object> claims = new HashMap<>();

    public authService(UserRepo userRepo, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

}

