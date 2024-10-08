package com.finalprogramacion.sistemaDeVuelos.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

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
    private List<SeatDTO> seats;
    private List<String> reservedSeats = new ArrayList<>(); // Para almacenar los números de asiento reservados
    private List<PassengerDTO> passengerDTOs = new ArrayList<>();

    // Método para obtener los asientos reservados
    public List<String> getReservedSeats() {
        return reservedSeats;
    }

    // Método para reservar un asiento
    public void reserveSeat(String seatNumber) {
        if (!reservedSeats.contains(seatNumber)) {
            reservedSeats.add(seatNumber);
        }
    }

    // Método para obtener la cantidad de pasajeros
        public int getPassengerCount() {
        if (this.passengers != null) {
            return this.passengers.size();  // Retorna el tamaño de la lista de pasajeros
        }
        return 0;  // Si la lista de pasajeros es nula, retornar 0
    }

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
