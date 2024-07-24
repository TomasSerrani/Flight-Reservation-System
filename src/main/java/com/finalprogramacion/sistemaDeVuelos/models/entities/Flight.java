package com.finalprogramacion.sistemaDeVuelos.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Data
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_num", nullable = false, unique = true)
    private String flightNum;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "available_seats")
    private int availableSeats;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "airway", nullable = false)
    private String airway;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "state", nullable = false)
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_id", nullable = false)
    private Airport origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Airport destination;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "flight_stopovers",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "airport_id")
    )
    private List<Airport> stopOvers= new ArrayList<>();

    @OneToMany(mappedBy = "flight_id",fetch = FetchType.LAZY)
    private List<Passenger> passenger;
}

