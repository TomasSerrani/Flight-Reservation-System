package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.AirportDTO;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Airport;
import com.finalprogramacion.sistemaDeVuelos.models.services.AirportService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.*;

@RestController
@RequestMapping("/airports")
public class AirportController {

    @Autowired
    private AirportService airportService;

    @GetMapping
    public List<AirportDTO> getAllAirports() {
        return airportService.getAllAirports().stream()
                .map(EntityAndDTOConverter::toAirportDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportDTO> getAirportById(@PathVariable Long id) {
        AirportDTO airportDTO = toAirportDTO(airportService.getAirportById(id));
        if (airportService.getAirportById(id) != null) {
            return ResponseEntity.ok(airportDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<AirportDTO> uploadAirport(@RequestBody AirportDTO airportDTO) {
        Airport savedAirport =dtoToAirport(airportDTO);
        airportService.createAirport(savedAirport);
        return ResponseEntity.status(HttpStatus.CREATED).body(airportDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirportDTO> updateAirport(@PathVariable Long id, @RequestBody AirportDTO airportDetails) {
        Airport updatedAirport = dtoToAirport(airportDetails);
        airportService.updateAirport(id, updatedAirport);
        if (airportService.getAirportById(id) != null) {
            return ResponseEntity.ok(airportDetails);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        if (airportService.getAirportById(id) != null) {
            airportService.deleteAirport(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
