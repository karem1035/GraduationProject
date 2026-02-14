package com.example.Toda.repo;

import com.example.Toda.DTO.TourGuideBasicInfoResponse;
import com.example.Toda.Entity.TourGuideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TourGuideRepo extends JpaRepository<TourGuideEntity, Long> {
    Optional<TourGuideEntity> findByEmail(String email);
    @Query("SELECT new com.example.Toda.DTO.TourGuideBasicInfoResponse(t.name, t.email) " +
            "FROM TourGuideEntity t WHERE t.email = :email")
    Optional<TourGuideBasicInfoResponse> findBasicInfoByEmail(@Param("email") String email);
}
