package com.finalprogramacion.sistemaDeVuelos.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_flight", unique = true, nullable = false)
    private String flightNum;

    @Column(name = "capacity_flight", nullable = false)
    private int capacity;

    @Column(name = "availableSeats_flight", nullable = false)
    private int availableSeats;

    @Column(name = "duration_flight", nullable = false)
    private String duration;

    @Column(name = "airway_flight", nullable = false)
    private String airway;

    @Column(name = "price_flight", nullable = false)
    private float price;

    @Column(name = "departureDate_flight", nullable = false)
    private Date departureDate;

    @Column(name = "state_flight", nullable = false)
    private String state;

    @ManyToOne
    @JoinColumn(name = "destination_id", referencedColumnName = "id")
    private Airport destination;

    @ManyToOne
    @JoinColumn(name = "origin_id", referencedColumnName = "id")
    private Airport origin;

    @ManyToMany
    @JoinTable(
            name = "flight_scales",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "airport_id")
    )
    private List<Airport> stopOvers;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats;
}

