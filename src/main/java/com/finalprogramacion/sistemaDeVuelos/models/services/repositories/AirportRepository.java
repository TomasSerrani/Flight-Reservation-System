package com.finalprogramacion.sistemaDeVuelos.models.services.repositories;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    Airport findByCity(String city);
}
