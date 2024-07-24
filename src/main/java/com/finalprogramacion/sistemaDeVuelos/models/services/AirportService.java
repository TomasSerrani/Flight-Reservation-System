package com.finalprogramacion.sistemaDeVuelos.models.services;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Airport;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    public Airport getAirportById(Long id) {
        Optional<Airport> airport = airportRepository.findById(id);
        return airport.orElse(null);
    }

    public Airport createAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    public Airport updateAirport(Long id, Airport airportDetails) {
        Optional<Airport> airport = airportRepository.findById(id);
        if (airport.isPresent()) {
            Airport existingAirport = airport.get();
            existingAirport.setName(airportDetails.getName());
            existingAirport.setCity(airportDetails.getCity());
            existingAirport.setCountry(airportDetails.getCountry());
            return airportRepository.save(existingAirport);
        }
            return null;
    }

    public boolean deleteAirport(Long id) {
        if (airportRepository.existsById(id)) {
            airportRepository.deleteById(id);
            return true;
        }
            return false;
    }
}
