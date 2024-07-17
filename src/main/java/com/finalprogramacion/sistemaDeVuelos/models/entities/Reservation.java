package com.finalprogramacion.sistemaDeVuelos.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reservation {
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

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    public Reservation(Long id, Long number, Date date, String state, User user, Payment payment) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.state = state;
        this.user = user;
        this.payment = payment;
    }
}
