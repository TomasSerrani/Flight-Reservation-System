package com.finalprogramacion.sistemaDeVuelos.models.services.repositories;

import com.finalprogramacion.sistemaDeVuelos.models.entities.User;
import com.finalprogramacion.sistemaDeVuelos.models.entities.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {
    UserDetails findByEmail(String email);
}
