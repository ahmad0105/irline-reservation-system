package com.airline.reservation.service;

import com.airline.reservation.dto.FlightDto;
import com.airline.reservation.dto.FlightRequest;
import com.airline.reservation.entity.Airport;
import com.airline.reservation.entity.Flight;
import com.airline.reservation.repository.AirportRepository;
import com.airline.reservation.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final AirportService airportService;

    public FlightDto createFlight(FlightRequest request) {
        // checking if flight number already exists
        if (flightRepository.existsByFlightNumber(request.getFlightNumber())) {
            throw new IllegalArgumentException("Flight number already exists!");
        }

        // get departure airport from db
        Airport depAirport = airportRepository.findById(request.getDepartureAirportId())
                .orElseThrow(() -> new IllegalArgumentException("Departure Airport not found"));

        // get arrival airport from db
        Airport arrAirport = airportRepository.findById(request.getArrivalAirportId())
                .orElseThrow(() -> new IllegalArgumentException("Arrival Airport not found"));

        // airports should not be the same
        if (depAirport.getId().equals(arrAirport.getId())) {
            throw new IllegalArgumentException("Departure and Arrival airports cannot be the same");
        }

        // build the flight entity
        Flight flight = new Flight();
        flight.setFlightNumber(request.getFlightNumber());
        flight.setDepartureAirport(depAirport);
        flight.setArrivalAirport(arrAirport);
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setAvailableSeats(request.getAvailableSeats());
        flight.setPrice(request.getPrice());

        // save flight
        Flight savedFlight = flightRepository.save(flight);
        
        return mapToDto(savedFlight);
    }

    public List<FlightDto> getAllFlights() {
        // get all flights and convert them to DTOs
        List<Flight> flights = flightRepository.findAll();
        List<FlightDto> dtoList = new ArrayList<>();
        
        for (Flight f : flights) {
            dtoList.add(mapToDto(f));
        }
        
        return dtoList;
    }

    public FlightDto getFlightById(Long id) {
        // find flight by id
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + id));
        return mapToDto(flight);
    }

    public FlightDto updateFlight(Long id, FlightRequest request) {
        // get existing flight
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + id));

        Airport depAirport = airportRepository.findById(request.getDepartureAirportId())
                .orElseThrow(() -> new IllegalArgumentException("Departure Airport not found"));

        Airport arrAirport = airportRepository.findById(request.getArrivalAirportId())
                .orElseThrow(() -> new IllegalArgumentException("Arrival Airport not found"));

        // update the values
        flight.setDepartureAirport(depAirport);
        flight.setArrivalAirport(arrAirport);
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setAvailableSeats(request.getAvailableSeats());
        flight.setPrice(request.getPrice());

        // save the updated flight
        Flight updatedFlight = flightRepository.save(flight);
        return mapToDto(updatedFlight);
    }

    public void deleteFlight(Long id) {
        // delete flight if it exists
        if (!flightRepository.existsById(id)) {
            throw new IllegalArgumentException("Flight not found with id: " + id);
        }
        flightRepository.deleteById(id);
    }

    public List<FlightDto> searchFlights(String from, String to) {
        // Find flights starting from now
        List<Flight> flights = flightRepository.findByDepartureAirportCodeAndArrivalAirportCodeAndDepartureTimeAfter(
                from, to, LocalDateTime.now());
                
        List<FlightDto> dtoList = new ArrayList<>();
        for (Flight f : flights) {
            dtoList.add(mapToDto(f));
        }
        
        return dtoList;
    }

    // map entity to dto method
    public FlightDto mapToDto(Flight flight) {
        FlightDto dto = new FlightDto();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setDepartureAirport(airportService.mapToDto(flight.getDepartureAirport()));
        dto.setArrivalAirport(airportService.mapToDto(flight.getArrivalAirport()));
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setAvailableSeats(flight.getAvailableSeats());
        dto.setPrice(flight.getPrice());
        return dto;
    }
}
