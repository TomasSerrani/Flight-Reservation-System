package com.finalprogramacion.sistemaDeVuelos.models.services;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Airport;
import com.finalprogramacion.sistemaDeVuelos.models.entities.Flight;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.AirportRepository;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.FlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    public List<Flight> getAllFlights() {
        List<Flight> flights= new ArrayList<>();

        return flightRepository.findAll();
    }

    public Flight getFlightById(Long id) {
        Optional<Flight> flight = flightRepository.findById(id);
        return flight.orElse(null);
    }

    @Transactional
    public Flight createFlight(Flight flight) {
        // Save the origin and destination airports first if they are new
        if (flight.getOrigin() != null && flight.getOrigin().getId() == null) {
            airportRepository.save(flight.getOrigin());
        }
        if (flight.getDestination() != null && flight.getDestination().getId() == null) {
            airportRepository.save(flight.getDestination());
        }

        // Now save the flight entity
        return flightRepository.save(flight);
    }

    public Flight updateFlight(Long id, Flight flightDetails) {
        Optional<Flight> flight = flightRepository.findById(id);
        if (flight.isPresent()) {
            Flight existingFlight = flight.get();
            existingFlight.setFlightNum(flightDetails.getFlightNum());
            existingFlight.setCapacity(flightDetails.getCapacity());
            existingFlight.setAvailableSeats(flightDetails.getAvailableSeats());
            existingFlight.setDuration(flightDetails.getDuration());
            existingFlight.setAirway(flightDetails.getAirway());
            existingFlight.setPrice(flightDetails.getPrice());
            existingFlight.setDepartureDate(flightDetails.getDepartureDate());
            existingFlight.setState(flightDetails.getState());
            return flightRepository.save(existingFlight);
        }
        return null;
    }

    public boolean deleteFlight(Long id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<Flight> findByOrigin(String originName) {
        Airport origin = airportRepository.findByCity(originName);
        if (origin != null) {
            return flightRepository.findByOrigin(origin);
        }
        return null;
    }
    public List<Flight> findByDestination(String destinationName){
        Airport destination = airportRepository.findByCity(destinationName);
        if(destination != null){
            return flightRepository.findByDestination(destination);
        }
        return null;

    }
}