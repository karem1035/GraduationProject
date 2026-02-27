package com.example.Toda.repo;

import com.example.Toda.Entity.TouristProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TouristProfileRepo extends JpaRepository<TouristProfileEntity, Long> {
    Optional<TouristProfileEntity> findByUserId(Long userId);
    List<TouristProfileEntity> findAllByUserId(Long userId);
    Optional<TouristProfileEntity> findByEmail(String email);
}
