package com.finalprogramacion.sistemaDeVuelos.models.services.repositories;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByNumber(Long number);
    List<Payment> findByReservationId(Long reservationId);
}
