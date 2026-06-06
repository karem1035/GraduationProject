package com.example.Toda.repo;

import com.example.Toda.Entity.StaticTrip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaticTripRepository extends JpaRepository<StaticTrip, Long> {
    Page<StaticTrip> findAllByOrderByStartDateDesc(Pageable pageable);
}