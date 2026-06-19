package com.airline.reservation.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlightDto {
    private Long id;
    private String flightNumber;
    private AirportDto departureAirport;
    private AirportDto arrivalAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;
    private BigDecimal price;
}
