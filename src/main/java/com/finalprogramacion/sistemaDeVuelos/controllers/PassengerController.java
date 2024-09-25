package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.PassengerDTO;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Passenger;
import com.finalprogramacion.sistemaDeVuelos.models.services.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.dtoToPassenger;
import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.toPassengerDTO;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @GetMapping
    public List<PassengerDTO> findAll() {
        return passengerService.getAllPassenger().stream()
                .map(EntityAndDTOConverter::toPassengerDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDTO> findById(@PathVariable Long id) {
        PassengerDTO passenger = toPassengerDTO(passengerService.getPassengerById(id));
        if (passengerService.getPassengerById(id) != null) {
            return ResponseEntity.ok(passenger);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PassengerDTO> save(@RequestBody PassengerDTO passengerDTO) {
        Passenger savedPassenger= dtoToPassenger(passengerDTO);
        passengerService.createPassenger(savedPassenger);
        return ResponseEntity.status(HttpStatus.CREATED).body(passengerDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (passengerService.getPassengerById(id) != null) {
            passengerService.deletePassenger(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }

    public PassengerDTO findByEmail(String email){
        return toPassengerDTO(passengerService.findByEmail(email));
    }
}
