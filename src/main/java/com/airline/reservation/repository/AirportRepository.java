package com.airline.reservation.repository;

import com.airline.reservation.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByCode(String code);
    boolean existsByCode(String code);
}
