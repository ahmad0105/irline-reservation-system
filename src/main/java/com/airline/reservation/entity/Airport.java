package com.airline.reservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "airports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Builder.Default
    @OneToMany(mappedBy = "departureAirport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flight> departures = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "arrivalAirport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flight> arrivals = new ArrayList<>();
}
