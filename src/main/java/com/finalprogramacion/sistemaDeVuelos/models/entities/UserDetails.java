package com.finalprogramacion.sistemaDeVuelos.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_cvv")
    private String cardCVV;

    @Column(name = "card_expiry")
    private String cardExpiry;

    @Column(name = "cbu_number")
    private String cbuNumber;

    @Column(name = "bank_name")
    private String bankName;

    // One-to-one relationship with the User entity
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // New fields for password recovery
    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_expiry")
    private Date passwordResetExpiry;

    // Constructor with all fields
    public UserDetails(String email, String password, String phoneNumber, String cardExpiry, String cardNumber, String cardCVV, User user) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.cardExpiry = cardExpiry;
        this.cardNumber = cardNumber;
        this.cardCVV = cardCVV;
        this.user = user;
    }

    // Constructor for basic fields
    public UserDetails(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Constructor for registration with phone number
    public UserDetails(String email, String password, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    // Update credit card details
    public void updateCardDetails(String expiryDate, String cardNumber, String cvv) {
        this.cardExpiry = expiryDate;
        this.cardNumber = cardNumber;
        this.cardCVV = cvv;
    }

    // Update bank details
    public void updateBankDetails(String bankName, String cbuNumber) {
        this.bankName = bankName;
        this.cbuNumber = cbuNumber;
    }

    // New methods for handling password reset token and expiry
    public void setPasswordResetToken(String token) {
        this.passwordResetToken = token;
    }

    public void setPasswordResetExpiry(Date expiryDate) {
        this.passwordResetExpiry = expiryDate;
    }

    public void clearPasswordResetToken() {
        this.passwordResetToken = null;
        this.passwordResetExpiry = null;
    }
}
