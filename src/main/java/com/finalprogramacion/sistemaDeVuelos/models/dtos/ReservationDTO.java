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
    private Long userId;
    private Long paymentId;
    private Long flightId;
}
