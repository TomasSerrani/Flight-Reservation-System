package com.finalprogramacion.sistemaDeVuelos.models.services;

import com.finalprogramacion.sistemaDeVuelos.models.entities.UserDetails;
import com.finalprogramacion.sistemaDeVuelos.models.services.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public List<UserDetails> getAllUserDetails() {
        return userDetailsRepository.findAll();
    }

    public UserDetails getUserDetailsById(Long id) {
        Optional<UserDetails> userDetails = userDetailsRepository.findById(id);
        return userDetails.orElse(null);
    }

    public UserDetails getUserbyEmail(String email) {
        Optional<UserDetails> userDetails = Optional.ofNullable(userDetailsRepository.findByEmail(email));
        return userDetails.orElse(null);
    }

    public UserDetails createUserDetails(UserDetails userDetails) {
        return userDetailsRepository.save(userDetails);
    }

    public UserDetails updateUserDetails(Long id, UserDetails userDetailsDetails) {
        Optional<UserDetails> userDetails = userDetailsRepository.findById(id);
        if (userDetails.isPresent()) {
            UserDetails existingUserDetails = userDetails.get();
            existingUserDetails.setEmail(userDetailsDetails.getEmail());
            existingUserDetails.setPassword(userDetailsDetails.getPassword());
            existingUserDetails.setPhoneNumber(userDetailsDetails.getPhoneNumber());
            existingUserDetails.setCardNumber(userDetailsDetails.getCardNumber());
            existingUserDetails.setCbuNumber(userDetailsDetails.getCbuNumber());
            return userDetailsRepository.save(existingUserDetails);
        }
            return null;
    }

    public boolean deleteUserDetails(Long id) {
        if (userDetailsRepository.existsById(id)) {
            userDetailsRepository.deleteById(id);
            return true;
        }
            return false;
    }

    public UserDetails authenticate(String email, String password) {
        UserDetails userDetails = userDetailsRepository.findByEmail(email);
        if (userDetails != null && userDetails.getPassword().equals(password)) {
            return userDetails;
        }
        return null;
    }
}
