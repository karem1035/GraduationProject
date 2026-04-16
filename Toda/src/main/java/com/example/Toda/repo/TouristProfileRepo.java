package com.example.Toda.repo;

import com.example.Toda.DTO.TourBasicInfoResponse;
import com.example.Toda.DTO.TourGuideBasicInfoResponse;
import com.example.Toda.Entity.TouristProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TouristProfileRepo extends JpaRepository<TouristProfileEntity, Long> {
    Optional<TouristProfileEntity> findByUserId(Long userId);
    List<TouristProfileEntity> findAllByUserId(Long userId);
    Optional<TouristProfileEntity> findByEmail(String email);
    @Query("SELECT new com.example.Toda.DTO.TourBasicInfoResponse(t.name, t.email) " +
            "FROM TouristProfileEntity t WHERE t.email = :email")
    Optional<TourBasicInfoResponse> findBasicInfoByEmail(@Param("email") String email);
}
