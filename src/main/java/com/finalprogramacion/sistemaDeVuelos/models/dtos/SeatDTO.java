package com.finalprogramacion.sistemaDeVuelos.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SeatDTO {
    private Long id;
    private String seatNumber;
    private String classType;
    private FlightDTO flight;
    private PassengerDTO passenger;
}
