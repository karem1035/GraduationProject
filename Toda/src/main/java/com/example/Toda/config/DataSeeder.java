package com.example.Toda.config;

import com.example.Toda.Entity.Role;
import com.example.Toda.Entity.UserEntity;
import com.example.Toda.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user already exists
        if (userRepo.findByEmail("admin@toda.com").isEmpty()) {
            // Create admin user
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setEmail("admin@toda.com");
            admin.setPassword(passwordEncoder.encode("0000"));
            admin.setRole(Role.ADMIN);
            
            userRepo.save(admin);
            
            logger.info("Admin user created successfully!");
            logger.info("Email: admin@toda.com");
            logger.info("Password: 0000");
        } else {
            logger.info("Admin user already exists. Skipping creation.");
        }
    }
}