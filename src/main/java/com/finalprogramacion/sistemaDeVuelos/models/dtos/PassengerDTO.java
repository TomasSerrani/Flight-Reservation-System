package com.finalprogramacion.sistemaDeVuelos.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PassengerDTO {
    private Long id;
    private String name;
    private String email;
    private Long seatId;
    private UserDTO user;
    private FlightDTO flight;

    @Override
    public String toString() {
        return "PassengerDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", seatId=" + seatId +
                ", user=" + user +
                ", flight=" + flight +
                '}';
    }
}
