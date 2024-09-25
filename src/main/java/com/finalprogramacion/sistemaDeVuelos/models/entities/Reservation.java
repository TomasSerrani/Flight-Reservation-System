package com.finalprogramacion.sistemaDeVuelos.models.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Random;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reservation {
    public Long getId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", unique = true)
    private Long number;

    @Column(name = "date")
    private Date date;

    @Column(name = "state")
    private String state;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Use @JsonManagedReference to manage the circular relationship
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Prevents infinite recursion during serialization
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "flight_id", referencedColumnName = "id")
    private Flight flight;

    public void setPaymentID(Long id){
        if (this.payment != null) {
            this.payment.setId(id);
        }
    }

    // Additional constructors
    public Reservation(Long number, Date date, String state, User user, Payment payment, Flight flight) {
        this.number = number;
        this.date = date;
        this.state = state;
        this.user = user;
        this.payment = payment;
        this.flight = flight;
    }

    // Method to generate a reservation number
    public Long generateReservationNumber() {
        Random RANDOM = new Random();
        long min = (long) Math.pow(10, 12 - 1);
        long max = (long) Math.pow(10, 12) - 1;
        return Math.abs(min + RANDOM.nextLong() % (max - min + 1));
    }
}
