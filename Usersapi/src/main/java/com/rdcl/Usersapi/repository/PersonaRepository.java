package com.rdcl.Usersapi.repository;

import com.rdcl.Usersapi.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona,Integer> {
}