package com.airline.reservation.dto;

import com.airline.reservation.entity.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    private Long userId;
    private FlightDto flight;
    private LocalDateTime bookingDate;
    private BookingStatus status;
}
