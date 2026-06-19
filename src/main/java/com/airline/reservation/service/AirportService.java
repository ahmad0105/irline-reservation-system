package com.airline.reservation.service;

import com.airline.reservation.dto.AirportDto;
import com.airline.reservation.entity.Airport;
import com.airline.reservation.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportDto createAirport(AirportDto airportDto) {
        // checking if code already exists
        if (airportRepository.existsByCode(airportDto.getCode())) {
            throw new IllegalArgumentException("Airport code already exists!");
        }

        // create airport entity
        Airport airport = new Airport();
        airport.setCode(airportDto.getCode());
        airport.setName(airportDto.getName());
        airport.setCity(airportDto.getCity());
        airport.setCountry(airportDto.getCountry());

        // save it to db
        Airport savedAirport = airportRepository.save(airport);
        
        return mapToDto(savedAirport);
    }

    public List<AirportDto> getAllAirports() {
        // get all airports from database
        List<Airport> airports = airportRepository.findAll();
        List<AirportDto> dtoList = new ArrayList<>();
        
        // convert entities to dtos using a for loop
        for (Airport airport : airports) {
            AirportDto dto = mapToDto(airport);
            dtoList.add(dto);
        }
        
        return dtoList;
    }

    public AirportDto getAirportById(Long id) {
        // find airport by id
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Airport not found with id: " + id));
                
        return mapToDto(airport);
    }

    // helper method to map entity to dto
    public AirportDto mapToDto(Airport airport) {
        AirportDto dto = new AirportDto();
        dto.setId(airport.getId());
        dto.setCode(airport.getCode());
        dto.setName(airport.getName());
        dto.setCity(airport.getCity());
        dto.setCountry(airport.getCountry());
        return dto;
    }
}
