package com.example.Toda.repo;

import com.example.Toda.Entity.TouristProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouristProfileRepo extends JpaRepository<TouristProfileEntity, Long> {
    List<TouristProfileEntity> findByUserId(Long userId);
}
