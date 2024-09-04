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
        dto.setDepartureTime(flight.getDepartureTime());
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
        entity.setDepartureTime(flightDTO.getDepartureTime());

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
        dto.setName(airport.getName() != null ? airport.getName() : "");
        dto.setCity(airport.getCity());
        dto.setCountry(airport.getCountry());
        return dto;
    }

    public static Airport dtoToAirport(AirportDTO airportDTO) {
        Airport entity = new Airport();
        entity.setId(airportDTO.getId());
        entity.setName(airportDTO.getName());
        entity.setCity(airportDTO.getCity());
        entity.setCountry(airportDTO.getCountry());
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

    public static PaymentDTO toPaymentDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setState(payment.getState());
        dto.setType(payment.getType());
        dto.setNumber(payment.getNumber());
        dto.setAmountOfPayments(payment.getAmountOfPayments());
        dto.setReservation(toReservationDTO(payment.getReservation()));
        dto.setUser(toUserDTO(payment.getUser()));
        return dto;
    }
    public static Payment dtoToPayment(PaymentDTO paymentDTO) {
        Payment entity = new Payment();
        entity.setId(paymentDTO.getId());
        entity.setState(paymentDTO.getState());
        entity.setType(paymentDTO.getType());
        entity.setNumber(paymentDTO.getNumber());
        entity.setAmountOfPayments(paymentDTO.getAmountOfPayments());
        entity.setReservation(dtoToReservation(paymentDTO.getReservation()));
        entity.setUser(dtoToUser(paymentDTO.getUser()));
        return entity;
    }

    public static ReservationDTO toReservationDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setState(reservation.getState());
        dto.setDate(reservation.getDate());
        dto.setNumber(reservation.getNumber());
        dto.setFlight(toFlightDTO(reservation.getFlight()));
        dto.setPayment(toPaymentDTO(reservation.getPayment()));
        dto.setUser(toUserDTO(reservation.getUser()));
        return dto;
    }

    public static Reservation dtoToReservation(ReservationDTO reservationDTO) {
        Reservation entity = new Reservation();
        entity.setId(reservationDTO.getId());
        entity.setState(reservationDTO.getState());
        entity.setDate(reservationDTO.getDate());
        entity.setNumber(reservationDTO.getNumber());
        entity.setFlight(dtoToFlight(reservationDTO.getFlight()));
        entity.setPayment(dtoToPayment(reservationDTO.getPayment()));
        entity.setUser(dtoToUser(reservationDTO.getUser()));
        return entity;
    }
    public static UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setUserDetails(toUserDetailsDTO(user.getUserDetails()));

        if (user.getPayments() != null) {
            List<PaymentDTO> paymentDTOList = user.getPayments().stream().map(payment -> {
                PaymentDTO paymentDTO= new PaymentDTO();
                paymentDTO.setType(payment.getType());
                paymentDTO.setNumber(payment.getNumber());
                paymentDTO.setState(payment.getState());
                paymentDTO.setId(payment.getId());
                paymentDTO.setAmountOfPayments(payment.getAmountOfPayments());
                return paymentDTO;
            }).collect(Collectors.toList());
            dto.setPayments(paymentDTOList);
        }

        if (user.getReservations() != null){
                List<ReservationDTO> reservationDTOList = user.getReservations().stream().map(reservation -> {
                    ReservationDTO reservationDTO= new ReservationDTO();
                    reservationDTO.setDate(reservation.getDate());
                    reservationDTO.setNumber(reservation.getNumber());
                    reservationDTO.setState(reservation.getState());
                    reservationDTO.setId(reservation.getId());
                    return reservationDTO;
                }).collect(Collectors.toList());
                dto.setReservations(reservationDTOList);
        }
        return dto;
    }

    public static User dtoToUser(UserDTO userDTO) {
        User entity = new User();
        entity.setId(userDTO.getId());
        entity.setName(userDTO.getName());
        entity.setDateOfBirth(userDTO.getDateOfBirth());
        entity.setUserDetails(dtoToUserDetails(userDTO.getUserDetails()));

        if (userDTO.getPayments() != null) {
            List<Payment> paymentList = userDTO.getPayments().stream().map(paymentDTO -> {
                Payment payment= new Payment();
                payment.setType(paymentDTO.getType());
                payment.setNumber(paymentDTO.getNumber());
                payment.setState(paymentDTO.getState());
                payment.setId(paymentDTO.getId());
                payment.setAmountOfPayments(paymentDTO.getAmountOfPayments());
                return payment;
            }).collect(Collectors.toList());
            entity.setPayments(paymentList);
        }

        if (userDTO.getReservations() != null){
            List<Reservation> reservationList = userDTO.getReservations().stream().map(reservationDTO -> {
                Reservation reservation= new Reservation();
                reservation.setDate(reservationDTO.getDate());
                reservation.setNumber(reservationDTO.getNumber());
                reservation.setState(reservationDTO.getState());
                reservation.setId(reservationDTO.getId());
                return reservation;
            }).collect(Collectors.toList());
            entity.setReservations(reservationList);
        }
        return entity;
    }

    public static PassengerDTO toPassengerDTO(Passenger passenger) {
        PassengerDTO dto = new PassengerDTO();
        dto.setId(passenger.getId());
        dto.setFirstName(passenger.getFirstName());
        dto.setLastName(passenger.getLastName());
        dto.setUser(toUserDTO(passenger.getUser()));
        dto.setFlight(toFlightDTO(passenger.getFlight()));
        return dto;
    }

    public static Passenger dtoToPassenger(PassengerDTO passengerDTO) {
        Passenger entity = new Passenger();
        entity.setId(passengerDTO.getId());
        entity.setFirstName(passengerDTO.getFirstName());
        entity.setLastName(passengerDTO.getLastName());
        entity.setUser(dtoToUser(passengerDTO.getUser()));
        entity.setFlight(dtoToFlight(passengerDTO.getFlight()));
        return entity;
    }

    public static SeatDTO toSeatDTO(Seat seat) {
        SeatDTO dto = new SeatDTO();
        dto.setId(seat.getId());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setPassenger(toPassengerDTO(seat.getPassenger()));
        dto.setFlight(toFlightDTO(seat.getFlight()));
        return dto;
    }

    public static Seat dtoToSeat(SeatDTO seatDTO) {
        Seat entity = new Seat();
        entity.setId(seatDTO.getId());
        entity.setSeatNumber(seatDTO.getSeatNumber());
        entity.setPassenger(dtoToPassenger(seatDTO.getPassenger()));
        entity.setFlight(dtoToFlight(seatDTO.getFlight()));
        return entity;
    }
}
