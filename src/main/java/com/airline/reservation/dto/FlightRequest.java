package com.airline.reservation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlightRequest {

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotNull(message = "Departure airport ID is required")
    private Long departureAirportId;

    @NotNull(message = "Arrival airport ID is required")
    private Long arrivalAirportId;

    @NotNull(message = "Departure time is required")
    @Future(message = "Departure time must be in the future")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @Future(message = "Arrival time must be in the future")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Available seats is required")
    @Min(value = 1, message = "Available seats must be at least 1")
    private Integer availableSeats;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private BigDecimal price;
}
