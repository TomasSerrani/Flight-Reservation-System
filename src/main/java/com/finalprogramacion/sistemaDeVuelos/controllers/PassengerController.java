package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Passenger;
import com.finalprogramacion.sistemaDeVuelos.models.services.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @GetMapping
    public List<Passenger> findAll() {
        return passengerService.getAllPassenger();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> findById(@PathVariable Long id) {
        Passenger passenger = passengerService.getPassengerById(id);
        if (passenger != null) {
            return ResponseEntity.ok(passenger);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Passenger save(@RequestBody Passenger passenger) {
        return passengerService.createPassenger(passenger);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}
