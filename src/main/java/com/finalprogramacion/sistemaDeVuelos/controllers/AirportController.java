package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Airport;
import com.finalprogramacion.sistemaDeVuelos.models.services.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airports")
public class AirportController {

    @Autowired
    private AirportService airportService;

    @GetMapping
    public List<Airport> getAllAirports() {
        return airportService.getAllAirports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airport> getAirportById(@PathVariable Long id) {
        Airport airport = airportService.getAirportById(id);
        if (airport != null) {
            return ResponseEntity.ok(airport);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Airport> uploadAirport(@RequestBody Airport airport) {
        Airport savedAirport = airportService.createAirport(airport);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAirport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Airport> updateAirport(@PathVariable Long id, @RequestBody Airport airportDetails) {
        Airport updatedAirport = airportService.updateAirport(id, airportDetails);
        if (updatedAirport != null) {
            return ResponseEntity.ok(updatedAirport);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        if (airportService.deleteAirport(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
