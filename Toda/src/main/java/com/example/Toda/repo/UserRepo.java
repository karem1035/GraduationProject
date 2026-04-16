package com.example.Toda.repo;

import com.example.Toda.DTO.TourGuideBasicInfoResponse;
import com.example.Toda.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);
    @Query("SELECT new com.example.Toda.DTO.TourGuideBasicInfoResponse(t.Username, t.email) " +
            "FROM UserEntity t WHERE t.email = :email")
    Optional<TourGuideBasicInfoResponse> findBasicInfoByEmail(@Param("email") String email);

    void deleteByIsDeletedTrueAndDeletionDateBefore(LocalDateTime now);
}
