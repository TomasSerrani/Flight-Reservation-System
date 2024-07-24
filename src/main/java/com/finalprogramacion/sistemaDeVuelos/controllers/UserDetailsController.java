package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.models.entities.User;
import com.finalprogramacion.sistemaDeVuelos.models.entities.UserDetails;
import com.finalprogramacion.sistemaDeVuelos.models.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userdetails")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping
    public List<UserDetails> findAll() {
        return userDetailsService.getAllUserDetails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetails> findById(@PathVariable Long id) {
        UserDetails userDetails = userDetailsService.getUserDetailsById(id);
        if (userDetails != null) {
            return ResponseEntity.ok(userDetails);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public UserDetails save(@RequestBody UserDetails userDetails) {
        return userDetailsService.createUserDetails(userDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userDetailsService.deleteUserDetails(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public UserDetails login(String email, String password) {
        return userDetailsService.authenticate(email, password);
    }
}
