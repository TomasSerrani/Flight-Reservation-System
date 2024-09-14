package com.finalprogramacion.sistemaDeVuelos.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", unique = true)
    private Long number;

    @Column(name = "type")
    private String type;

    @Column(name = "state")
    private String state;

    @Column(name = "amount_of_payments")
    private int amountOfPayments;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "payment")
    private Reservation reservation;

    public Payment(Long id, Long number, String type, String state, int amountOfPayments, User user, Reservation reservation) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.state = state;
        this.amountOfPayments = amountOfPayments;
        this.user = user;
        this.reservation = reservation;
    }

    public Payment(Long number, String type, String state, int amountOfPayments, User user) {
        this.number = number;
        this.type = type;
        this.state = state;
        this.amountOfPayments = amountOfPayments;
        this.user = user;
    }

}
