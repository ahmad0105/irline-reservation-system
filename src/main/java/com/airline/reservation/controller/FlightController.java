package com.airline.reservation.controller;

import com.airline.reservation.dto.FlightDto;
import com.airline.reservation.dto.FlightRequest;
import com.airline.reservation.dto.MessageResponse;
import com.airline.reservation.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public ResponseEntity<List<FlightDto>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDto> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightDto>> searchFlights(
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(flightService.searchFlights(from, to));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlightDto> createFlight(@Valid @RequestBody FlightRequest flightRequest) {
        return ResponseEntity.ok(flightService.createFlight(flightRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FlightDto> updateFlight(
            @PathVariable Long id,
            @Valid @RequestBody FlightRequest flightRequest) {
        return ResponseEntity.ok(flightService.updateFlight(id, flightRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok(new MessageResponse("Flight deleted successfully!"));
    }
}
