package com.airline.reservation.controller;

import com.airline.reservation.dto.BookingDto;
import com.airline.reservation.dto.BookingRequest;
import com.airline.reservation.dto.MessageResponse;
import com.airline.reservation.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(
            @Valid @RequestBody BookingRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(bookingService.createBooking(request, email));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingDto>> getMyBookings(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(bookingService.getMyBookings(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> cancelBooking(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        bookingService.cancelBooking(id, email);
        return ResponseEntity.ok(new MessageResponse("Booking cancelled successfully!"));
    }
}
