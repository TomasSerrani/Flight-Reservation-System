package com.finalprogramacion.sistemaDeVuelos.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FlightDTO {
    private Long id;
    private String flightNum;
    private int capacity;
    private int availableSeats;
    private String duration;
    private String airway;
    private double price;
    private LocalDate departureDate;
    private String state;
    private AirportDTO origin;
    private AirportDTO destination;
    private List<AirportDTO> stopOvers;
    private List<PassengerDTO> passengers;
}
