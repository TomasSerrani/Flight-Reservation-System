package com.finalprogramacion.sistemaDeVuelos.models.services;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Reservation;
import com.finalprogramacion.sistemaDeVuelos.models.entities.User;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.ReservationRepository;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly= true)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly= true)
    public Reservation getReservationById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.orElse(null);
    }

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            Reservation existingReservation = reservation.get();
            existingReservation.setNumber(reservationDetails.getNumber());
            existingReservation.setDate(reservationDetails.getDate());
            existingReservation.setState(reservationDetails.getState());
            existingReservation.setPayment(reservationDetails.getPayment());
            return reservationRepository.save(existingReservation);
        }
            return null;
    }

    @Transactional
    public boolean deleteReservation(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return true;
        }
            return false;
    }

    @Transactional(readOnly= true)
    public List<Reservation> getUserReservations(Long id){
        User user= userRepository.getReferenceById(id);
        return user.getReservations();
    }

    @Transactional(readOnly= true)
    public Reservation getByReservationNumber(Long reservationNumber){
        return reservationRepository.findByNumber(reservationNumber);
    }
}
