package com.finalprogramacion.sistemaDeVuelos.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserDetails userDetails;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    public User(Long id, String name, Date dateOfBirth, UserDetails userDetails, List<Reservation> reservations, List<Payment> payments) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.userDetails = userDetails;
        this.reservations = reservations;
        this.payments = payments;
    }

    public User(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public User(String name, Date dateOfBirth, UserDetails userDetails) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.userDetails = userDetails;
    }
}
