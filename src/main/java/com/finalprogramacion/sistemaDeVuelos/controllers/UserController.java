package com.finalprogramacion.sistemaDeVuelos.controllers;

import com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter;
import com.finalprogramacion.sistemaDeVuelos.models.dtos.UserDTO;
import com.finalprogramacion.sistemaDeVuelos.models.entities.User;
import com.finalprogramacion.sistemaDeVuelos.models.entities.UserDetails;
import com.finalprogramacion.sistemaDeVuelos.models.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.finalprogramacion.sistemaDeVuelos.collectors.EntityAndDTOConverter.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(EntityAndDTOConverter::toUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = toUserDTO(userService.getUserById(id));
        if (userService.getUserById(id) != null) {
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/current")
    public UserDetails getCurrentUser(@RequestHeader("Authorization") String token) {
        return userService.getCurrentUser(token);
    }

    @PostMapping("/register")
    public UserDTO createUser(UserDTO userDTO) {
        User savedUser =dtoToUser(userDTO);
        userService.createUser(savedUser);
        return userDTO;
    }



    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDetails) {
        User updatedUser = dtoToUser(userDetails);
        userService.updateUser(id, updatedUser);
        if (userService.getUserById(id) != null) {
            return ResponseEntity.ok(userDetails);
        }
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id) != null) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
            return ResponseEntity.notFound().build();
    }

}
