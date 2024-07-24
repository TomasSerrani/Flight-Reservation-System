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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "country", nullable = false)
    private String country;

    @OneToMany(mappedBy = "origin", fetch = FetchType.LAZY)
    private List<Flight> originFlights;

    @OneToMany(mappedBy = "destination", fetch = FetchType.LAZY)
    private List<Flight> destinationFlights;

    @ManyToMany(mappedBy = "stopOvers",fetch = FetchType.LAZY)
    private List<Flight> stopOverFlights;
}