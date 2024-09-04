package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.FlightDTO;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Flight;
import com.finalprogramacion.sistemaDeVuelos.models.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.dtoToFlight;
import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.toFlightDTO;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public List<FlightDTO> getAllFlights() {
        return flightService.getAllFlights().stream()
                .map(EntityAndDTOConverter::toFlightDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable Long id) {
        FlightDTO flightDTO = toFlightDTO(flightService.getFlightById(id));
        if (flightService.getFlightById(id) != null) {
            return ResponseEntity.ok(flightDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<FlightDTO> uploadFlight(@RequestBody FlightDTO flightDTO) {
        try {
            Flight savedFlight = dtoToFlight(flightDTO);
            flightService.createFlight(savedFlight);
            return ResponseEntity.status(HttpStatus.CREATED).body(flightDTO);
        } catch (Exception e) {
            e.printStackTrace(); // Esto imprimirá la traza de la excepción en los logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@PathVariable Long id, @RequestBody FlightDTO flightDetails) {
        Flight updatedFlight = dtoToFlight(flightDetails);
        flightService.updateFlight(id, updatedFlight);
        if (flightService.getFlightById(id) != null) {
            return ResponseEntity.ok(flightDetails);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        if (flightService.getFlightById(id) != null) {
            flightService.deleteFlight(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/searchByOriginAndDestination")
    public List<FlightDTO> searchFlightsByOriginAndDestination(@RequestParam String origin, String destination) {
        return flightService.findByOriginAndDestination(origin, destination).stream()
                .map(EntityAndDTOConverter::toFlightDTO)
                .collect(Collectors.toList());
    }
}