package com.finalprogramacion.sistemaDeVuelos.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class UserDTO {
    private Long id;
    private String name;
    private Date dateOfBirth;
    private UserDetailsDTO userDetails;
    private List<ReservationDTO> reservations;
    private List<PaymentDTO> payments;
}
