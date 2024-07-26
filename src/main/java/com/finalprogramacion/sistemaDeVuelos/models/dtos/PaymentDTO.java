package com.finalprogramacion.sistemaDeVuelos.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
