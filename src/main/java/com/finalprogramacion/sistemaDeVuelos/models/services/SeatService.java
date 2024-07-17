package com.finalprogramacion.sistemaDeVuelos.models.services;


import com.finalprogramacion.sistemaDeVuelos.models.entities.Seat;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Seat getSeatById(Long id) {
        Optional<Seat> seat = seatRepository.findById(id);
        return seat.orElse(null);
    }

    public Seat createSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public Seat updateSeat(Long id, Seat seatDetails) {
        Optional<Seat> seat = seatRepository.findById(id);
        if (seat.isPresent()) {
            Seat existingSeat = seat.get();
            // Update seat properties here
            return seatRepository.save(existingSeat);
        }
            return null;
    }

    public boolean deleteSeat(Long id) {
        if (seatRepository.existsById(id)) {
            seatRepository.deleteById(id);
            return true;
        }
            return false;
    }
}
