package com.airline.reservation.controller;

import com.airline.reservation.dto.AirportDto;
import com.airline.reservation.service.AirportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @GetMapping
    public ResponseEntity<List<AirportDto>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportDto> getAirportById(@PathVariable Long id) {
        return ResponseEntity.ok(airportService.getAirportById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AirportDto> createAirport(@Valid @RequestBody AirportDto airportDto) {
        return ResponseEntity.ok(airportService.createAirport(airportDto));
    }
}
