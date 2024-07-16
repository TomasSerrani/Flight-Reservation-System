package com.finalprogramacion.sistemaDeVuelos.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "cbu_number", nullable = false)
    private String cbuNumber;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public UserDetails(Long id, String password, String phoneNumber, String cardNumber, String cbuNumber, User user) {
        this.id = id;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.cardNumber = cardNumber;
        this.cbuNumber = cbuNumber;
        this.user = user;
    }

    public UserDetails(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
