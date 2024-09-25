package com.finalprogramacion.sistemaDeVuelos.models.dtos;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@Setter
@Getter
public class ReservationDTO {
    private Long id;
    private Long number;
    private Date date;
    private String state;
    private UserDTO user;
    private PaymentDTO payment;
    private FlightDTO flight;

    public ReservationDTO(Long number, Date date, String state, UserDTO user, PaymentDTO payment, FlightDTO flight) {
        this.number = number;
        this.date = date;
        this.state = state;
        this.user = user;
        this.payment = payment;
        this.flight = flight;
    }

    public void setPaymentID(Long id) {
        if (this.payment != null) {
            this.payment.setId(id);
            this.payment.setReservation(null);
        }
    }
}
