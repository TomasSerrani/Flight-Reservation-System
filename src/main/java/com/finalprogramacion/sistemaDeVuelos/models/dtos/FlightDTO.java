package com.finalprogramacion.sistemaDeVuelos.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
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
    private Time departureTime;
    private AirportDTO origin;
    private AirportDTO destination;
    private List<AirportDTO> stopOvers;
    private List<PassengerDTO> passengers;

    @Override
    public String toString() {
        return "FlightDTO{" +
                "flightNum='" + flightNum + '\'' +
                ", capacity=" + capacity +
                ", availableSeats=" + availableSeats +
                ", duration='" + duration + '\'' +
                ", airway='" + airway + '\'' +
                ", price=" + price +
                ", departureDate=" + departureDate +
                ", state='" + state + '\'' +
                ", origin=" + origin +
                ", destination=" + destination +
                ", stopOvers=" + stopOvers +
                '}';
    }
}
