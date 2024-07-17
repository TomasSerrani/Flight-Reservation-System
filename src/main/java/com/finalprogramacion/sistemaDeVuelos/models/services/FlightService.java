package com.finalprogramacion.sistemaDeVuelos.models.services;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Flight;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Flight getFlightById(Long id) {
        Optional<Flight> flight = flightRepository.findById(id);
        return flight.orElse(null);
    }

    public Flight createFlight(Flight flight) {
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
}
