package com.airline.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {

    @NotNull(message = "Flight ID is required")
    private Long flightId;

}
