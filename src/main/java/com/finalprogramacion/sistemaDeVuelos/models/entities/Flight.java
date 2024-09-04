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

    @Column(name = "flight_num", unique = true)
    private String flightNum;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "available_seats")
    private int availableSeats;

    @Column(name = "duration")
    private String duration;

    @Column(name = "airway")
    private String airway;

    @Column(name = "price")
    private double price;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "state")
    private String state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origin_id")
    private Airport origin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_id")
    private Airport destination;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "flight_stopovers",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "airport_id")
    )
    private List<Airport> stopOvers;

    @OneToMany(mappedBy = "flight",fetch = FetchType.EAGER)
    private List<Passenger> passenger;
}

