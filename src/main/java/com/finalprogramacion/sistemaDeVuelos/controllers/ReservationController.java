package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.ReservationDTO;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Reservation;
import com.finalprogramacion.sistemaDeVuelos.models.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return reservationService.getAllReservations().stream()
                .map(EntityAndDTOConverter::toReservationDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ReservationDTO getReservationById(@PathVariable Long id) {
        ReservationDTO reservation = toReservationDTO(reservationService.getReservationById(id));
        return reservation;
    }

    @GetMapping("/user-reservations")
    public List<ReservationDTO> getUserReservations(@PathVariable Long id) {
        return reservationService.getUserReservations(id).stream()
                .map(EntityAndDTOConverter::toReservationDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation savedReservation =dtoToReservation(reservationDTO);
        reservationService.createReservation(savedReservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDetails) {
        Reservation updatedReservation = dtoToReservation(reservationDetails);
        reservationService.updateReservation(id, updatedReservation);
        if (reservationService.getReservationById(id) != null) {
            return ResponseEntity.ok(reservationDetails);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        if (reservationService.getReservationById(id) != null) {
            reservationService.deleteReservation(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping ("/{number}")
    public Reservation findByReservationNumber(Long reservationNumber){
        return reservationService.getByReservationNumber(reservationNumber);
    }
}
