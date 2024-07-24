package com.finalprogramacion.sistemaDeVuelos.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserDetailsDTO {
    private Long id;
    private String email;
    private String password;
    private String phoneNumber;
    private String cardNumber;
    private String cbuNumber;
    private Long userId;
}
