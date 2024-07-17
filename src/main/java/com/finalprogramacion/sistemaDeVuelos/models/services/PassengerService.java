package com.finalprogramacion.sistemaDeVuelos.models.services;

import com.finalprogramacion.sistemaDeVuelos.models.entities.Passenger;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public List<Passenger> getAllPassenger() {
        return passengerRepository.findAll();
    }

    public Passenger getPassengerById(Long id) {
        Optional<Passenger> passenger = passengerRepository.findById(id);
        return passenger.orElse(null);
    }

    public Passenger createPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    public Passenger updatePassenger(Long id, Passenger passengerDetails) {
        Optional<Passenger> passenger = passengerRepository.findById(id);
        if (passenger.isPresent()) {
            Passenger existingPassenger = passenger.get();
            existingPassenger.setFirstName(passengerDetails.getFirstName());
            existingPassenger.setUser(passengerDetails.getUser());
            existingPassenger.setLastName(passengerDetails.getLastName());
            return passengerRepository.save(existingPassenger);
        }
            return null;
    }

    public boolean deletePassenger(Long id) {
        if (passengerRepository.existsById(id)) {
            passengerRepository.deleteById(id);
            return true;
        }
            return false;
    }
}
