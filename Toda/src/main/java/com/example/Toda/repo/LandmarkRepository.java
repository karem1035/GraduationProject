package com.example.Toda.repo;

import com.example.Toda.Entity.Landmark;
import com.example.Toda.Entity.LandmarkType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandmarkRepository extends JpaRepository<Landmark, Long> {

    /**
     * Find all landmarks with optional filters (type, city, name).
     */
    @Query("SELECT l FROM Landmark l WHERE " +
            "(:type IS NULL OR l.type = :type) " +
            "AND (:city IS NULL OR LOWER(l.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
            "AND (:name IS NULL OR LOWER(l.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Landmark> findFiltered(
            @Param("type") LandmarkType type,
            @Param("city") String city,
            @Param("name") String name,
            Pageable pageable);

    /**
     * Find landmarks by their IDs.
     */
    List<Landmark> findByIdIn(List<Long> ids);

    /**
     * Find all landmarks in a specific city.
     */
    List<Landmark> findByCityIgnoreCase(String city);
}