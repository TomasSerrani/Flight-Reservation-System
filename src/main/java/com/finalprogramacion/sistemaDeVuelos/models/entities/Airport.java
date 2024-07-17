package com.finalprogramacion.sistemaDeVuelos.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "airports")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "airportName", nullable = false)
    private String airportName;

    @Column(name = "cityName", nullable = false)
    private String cityName;

    @Column(name = "countryName", nullable = false)
    private String countryName;

    @OneToOne(mappedBy = "origin")
    private Flight originFlight;

    @OneToOne(mappedBy = "destination")
    private Flight destinationFlight;

    @ManyToMany(mappedBy = "stopOvers")
    private List<Flight> stopOverFlights;
}