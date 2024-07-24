package com.finalprogramacion.sistemaDeVuelos.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AirportDTO {
    private Long id;
    private String name;
    private String city;
    private String country;
    private List<FlightDTO> originFlights;
    private List<FlightDTO> destinationFlights;
    private List<FlightDTO> stopOverFlights;
}
