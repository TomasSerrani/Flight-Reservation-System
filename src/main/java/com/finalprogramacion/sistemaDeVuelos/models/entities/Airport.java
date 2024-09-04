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

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @OneToMany(mappedBy = "origin", fetch = FetchType.EAGER)
    private List<Flight> originFlights;

    @OneToMany(mappedBy = "destination", fetch = FetchType.EAGER)
    private List<Flight> destinationFlights;

    @ManyToMany(mappedBy = "stopOvers",fetch = FetchType.EAGER)
    private List<Flight> stopOverFlights;
}