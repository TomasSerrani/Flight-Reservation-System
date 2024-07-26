package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.SeatDTO;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Seat;
import com.finalprogramacion.sistemaDeVuelos.models.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.*;

@RestController
@RequestMapping("/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping
    public List<SeatDTO> getAllSeats() {
        return seatService.getAllSeats().stream()
                .map(EntityAndDTOConverter::toSeatDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDTO> getSeatById(@PathVariable Long id) {
        SeatDTO seat = toSeatDTO(seatService.getSeatById(id));
        if (seatService.getSeatById(id) != null) {
            return ResponseEntity.ok(seat);
        }
            return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SeatDTO> createSeat(@RequestBody SeatDTO seatDTO) {
        Seat savedSeat =dtoToSeat(seatDTO);
        seatService.createSeat(savedSeat);
        return ResponseEntity.status(HttpStatus.CREATED).body(seatDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatDTO> updateSeat(@PathVariable Long id, @RequestBody SeatDTO seatDetails) {
        Seat updatedSeat = dtoToSeat(seatDetails);
        seatService.updateSeat(id, updatedSeat);
        if (seatService.getSeatById(id) != null) {
            return ResponseEntity.ok(seatDetails);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        if (seatService.getSeatById(id) != null) {
            seatService.deleteSeat(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
