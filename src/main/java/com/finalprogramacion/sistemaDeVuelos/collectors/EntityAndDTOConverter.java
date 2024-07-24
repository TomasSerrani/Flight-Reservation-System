package com.finalprogramacion.sistemaDeVuelos.collectors;

import com.finalprogramacion.sistemaDeVuelos.models.dtos.*;
import com.finalprogramacion.sistemaDeVuelos.models.entities.*;

import java.util.List;
import java.util.stream.Collectors;

public class EntityAndDTOConverter {
    public static FlightDTO toFlightDTO(Flight flight) {
        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNum(flight.getFlightNum());
        dto.setCapacity(flight.getCapacity());
        dto.setAvailableSeats(flight.getAvailableSeats());
        dto.setDuration(flight.getDuration());
        dto.setAirway(flight.getAirway());
        dto.setPrice(flight.getPrice());
        dto.setDepartureDate(flight.getDepartureDate());
        dto.setState(flight.getState());
        dto.setOrigin(toAirportDTO(flight.getOrigin()));
        dto.setDestination(toAirportDTO(flight.getDestination()));

        List<AirportDTO> stopOvers = flight.getStopOvers().stream().map(airport -> {
            AirportDTO stopOver= new AirportDTO();
            stopOver.setCountry(airport.getCountry());
            stopOver.setCity(airport.getCity());
            stopOver.setName(airport.getName());
            stopOver.setId(airport.getId());
            return stopOver;
        }).collect(Collectors.toList());
        dto.setStopOvers(stopOvers);

        return dto;
    }
    public static Flight dtoToFlight (FlightDTO flightDTO) {
        Flight entity = new Flight();
        entity.setId(flightDTO.getId());
        entity.setFlightNum(flightDTO.getFlightNum());
        entity.setCapacity(flightDTO.getCapacity());
        entity.setAvailableSeats(flightDTO.getAvailableSeats());
        entity.setDuration(flightDTO.getDuration());
        entity.setAirway(flightDTO.getAirway());
        entity.setPrice(flightDTO.getPrice());
        entity.setDepartureDate(flightDTO.getDepartureDate());
        entity.setState(flightDTO.getState());

        Airport origin= new Airport();
        origin.setCity(flightDTO.getOrigin().getCity());
        origin.setId(flightDTO.getOrigin().getId());
        origin.setName(flightDTO.getOrigin().getName());
        origin.setCountry(flightDTO.getOrigin().getCountry());
        entity.setOrigin(origin);

        Airport destination= new Airport();
        destination.setCity(flightDTO.getDestination().getCity());
        destination.setId(flightDTO.getDestination().getId());
        destination.setName(flightDTO.getDestination().getName());
        destination.setCountry(flightDTO.getDestination().getCountry());
        entity.setDestination(destination);

        List<Airport> stopOvers = flightDTO.getStopOvers().stream().map(airport -> {
            Airport stopOver= new Airport();
            stopOver.setCountry(airport.getCountry());
            stopOver.setCity(airport.getCity());
            stopOver.setName(airport.getName());
            stopOver.setId(airport.getId());
            return stopOver;
        }).collect(Collectors.toList());
        entity.setStopOvers(stopOvers);
        return entity;
    }

    public static AirportDTO toAirportDTO(Airport airport) {
        AirportDTO dto = new AirportDTO();
        dto.setId(airport.getId());
        dto.setName(airport.getName());
        dto.setCity(airport.getCity());
        dto.setCountry(airport.getCountry());
        return dto;
    }

    public static Airport dtoToAirport(AirportDTO airportDTO) {
        Airport entity = new Airport();
        entity.setId(entity.getId());
        entity.setName(entity.getName());
        entity.setCity(entity.getCity());
        entity.setCountry(entity.getCountry());
        return entity;
    }

    public static UserDetailsDTO toUserDetailsDTO(UserDetails userDetails) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setId(userDetails.getId());
        dto.setEmail(userDetails.getEmail());
        dto.setPassword(userDetails.getPassword());
        dto.setPhoneNumber(userDetails.getPhoneNumber());
        if (userDetails.getCbuNumber() != null && userDetails.getCardNumber() != null){
            dto.setCardNumber(userDetails.getCardNumber());
            dto.setCbuNumber(userDetails.getCbuNumber());
            return dto;
        }
        return dto;
    }

    public static UserDetails dtoToUserDetails(UserDetailsDTO userDetailsDTO) {
        UserDetails entity = new UserDetails();
        entity.setId(userDetailsDTO.getId());
        entity.setEmail(userDetailsDTO.getEmail());
        entity.setPassword(userDetailsDTO.getPassword());
        entity.setPhoneNumber(userDetailsDTO.getPhoneNumber());
        if (userDetailsDTO.getCbuNumber() != null && userDetailsDTO.getCardNumber() != null){
            entity.setCardNumber(userDetailsDTO.getCardNumber());
            entity.setCbuNumber(userDetailsDTO.getCbuNumber());
            return entity;
        }
        return entity;
    }

    public static PaymentDTO toPaymentDTO(UserDetails userDetails) {
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setId(userDetails.getId());
        dto.setEmail(userDetails.getEmail());
        dto.setPassword(userDetails.getPassword());
        dto.setPhoneNumber(userDetails.getPhoneNumber());
        if (userDetails.getCbuNumber() != null && userDetails.getCardNumber() != null){
            dto.setCardNumber(userDetails.getCardNumber());
            dto.setCbuNumber(userDetails.getCbuNumber());
            return dto;
        }
        return dto;
    }

    public static UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setUserDetails(toUserDetailsDTO(user.getUserDetails()));

        if (user.getPayments() != null) {

        }
    }
}
