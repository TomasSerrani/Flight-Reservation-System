package com.finalprogramacion.sistemaDeVuelos.models.services.repositories;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Airport;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByOriginCity(String originCity);
    List<Flight> findByDestinationCity(String destinationCity);

    List<Flight> findByOrigin(Airport origin);
    List<Flight> findByDestination(Airport destination);
}
