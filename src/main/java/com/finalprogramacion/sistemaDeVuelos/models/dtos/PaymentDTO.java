package com.finalprogramacion.sistemaDeVuelos.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;

import static org.hibernate.annotations.UuidGenerator.Style.RANDOM;

@NoArgsConstructor
@Getter
@Setter
public class PaymentDTO {
    private Long id;
    private Long number;
    private String type;
    private String state;
    private int amountOfPayments;
    private UserDTO user;
    private ReservationDTO reservation;

    public PaymentDTO(Long number, String type, int amountOfPayments, UserDTO user) {
        this.number = number;
        this.type = type;
        this.amountOfPayments = amountOfPayments;
        this.user = user;
    }

    public Long generatePaymentNumber() {
        Random RANDOM = new Random();
        long min = (long) Math.pow(10, 12 - 1);
        long max = (long) Math.pow(10, 12) - 1;
        return Math.abs(min + RANDOM.nextLong() % (max - min + 1));
    }
}
