package com.finalprogramacion.sistemaDeVuelos.models.services.repositories;

import com.finalprogramacion.sistemaDeVuelos.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
