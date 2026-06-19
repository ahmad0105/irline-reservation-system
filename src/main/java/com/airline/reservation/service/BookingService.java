package com.airline.reservation.service;

import com.airline.reservation.dto.BookingDto;
import com.airline.reservation.dto.BookingRequest;
import com.airline.reservation.entity.Booking;
import com.airline.reservation.entity.BookingStatus;
import com.airline.reservation.entity.Flight;
import com.airline.reservation.entity.User;
import com.airline.reservation.repository.BookingRepository;
import com.airline.reservation.repository.FlightRepository;
import com.airline.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final FlightService flightService;

    @Transactional
    public BookingDto createBooking(BookingRequest request, String email) {
        // get the user who is booking
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // get the flight
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new IllegalArgumentException("Flight not found"));

        // check if there are seats available
        if (flight.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("No available seats on this flight");
        }

        // Reduce available seats by 1
        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        // create new booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        // save to db
        Booking savedBooking = bookingRepository.save(booking);
        return mapToDto(savedBooking);
    }

    public List<BookingDto> getMyBookings(String email) {
        // find user first
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // find all bookings for this user
        List<Booking> bookings = bookingRepository.findByUserId(user.getId());
        List<BookingDto> dtoList = new ArrayList<>();
        
        for (Booking booking : bookings) {
            dtoList.add(mapToDto(booking));
        }
        
        return dtoList;
    }

    @Transactional
    public void cancelBooking(Long bookingId, String email) {
        // get the booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // check if the user owns this booking
        if (!booking.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("You are not authorized to cancel this booking");
        }

        // check if already cancelled
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Booking is already cancelled");
        }

        // Increase available seats back
        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        flightRepository.save(flight);

        // update status to cancelled
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    // helper method
    private BookingDto mapToDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setFlight(flightService.mapToDto(booking.getFlight()));
        dto.setBookingDate(booking.getBookingDate());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}
