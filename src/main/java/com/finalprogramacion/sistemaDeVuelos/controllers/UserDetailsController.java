package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.UserDetailsDTO;
import com.finalprogramacion.sistemaDeVuelos.models.entities.UserDetails;
import com.finalprogramacion.sistemaDeVuelos.models.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.*;
import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.toUserDetailsDTO;

@RestController
@RequestMapping("/userdetails")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping
    public List<UserDetailsDTO> findAll() {
        return userDetailsService.getAllUserDetails().stream()
                .map(EntityAndDTOConverter::toUserDetailsDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> findById(@PathVariable Long id) {
        UserDetailsDTO userDetailsDTO = toUserDetailsDTO(userDetailsService.getUserDetailsById(id));
        if (userDetailsService.getUserDetailsById(id) != null) {
            return ResponseEntity.ok(userDetailsDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserDetailsDTO> save(@RequestBody UserDetailsDTO userDetails) {
        UserDetails savedUserDetails =dtoToUserDetails(userDetails);
        userDetailsService.createUserDetails(savedUserDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (userDetailsService.getUserDetailsById(id) != null) {
            userDetailsService.deleteUserDetails(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public UserDetails login(String email, String password) {
        return userDetailsService.authenticate(email, password);
    }

    @GetMapping("/{email}")
    public UserDetails findByEmail(@PathVariable String email) {
        UserDetails userDetails = userDetailsService.getUserbyEmail(email);
        return userDetails;
    }

}
