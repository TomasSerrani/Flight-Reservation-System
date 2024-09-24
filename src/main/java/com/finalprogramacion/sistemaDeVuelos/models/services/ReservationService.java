package com.finalprogramacion.sistemaDeVuelos.models.services;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Payment;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Reservation;
import com.finalprogramacion.sistemaDeVuelos.models.entities.User;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.ReservationRepository;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.orElse(null);
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, Reservation reservationDetails) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            Reservation existingReservation = reservation.get();
            existingReservation.setNumber(reservationDetails.getNumber());
            existingReservation.setDate(reservationDetails.getDate());
            existingReservation.setState(reservationDetails.getState());
            return reservationRepository.save(existingReservation);
        }
        return reservationDetails;
    }

    public boolean deleteReservation(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return true;
        }
            return false;
    }
    public void deleteByPaymentId(Long paymentId) {
        Reservation reservation = reservationRepository.findById(paymentId)
                .orElseThrow(() -> new NoSuchElementException("No reservation found for payment ID " + paymentId));
        reservationRepository.delete(reservation);
    }

    @Transactional // Asegúrate de tener esta anotación
    public List<Reservation> getUserReservations(Long id) {
        User user = userRepository.findById(id).orElse(null); // Cambia a findById
        if (user != null) {
            return user.getReservations(); // Esto ahora debería funcionar sin problemas
        }
        return Collections.emptyList(); // Manejar caso de usuario no encontrado
    }

    public Reservation getByReservationNumber(Long reservationNumber){
        return reservationRepository.findByNumber(reservationNumber);
    }
}
