package com.finalprogramacion.sistemaDeVuelos.models.services.repositories;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
}
