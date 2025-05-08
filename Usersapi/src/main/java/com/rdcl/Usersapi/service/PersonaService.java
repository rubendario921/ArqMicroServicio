package com.rdcl.Usersapi.service;


import com.rdcl.Usersapi.entity.Persona;
import com.rdcl.Usersapi.repository.PersonaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class PersonaService implements IPersonaService {
    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Async
    @Override
    public CompletableFuture<Persona> saveData(Persona persona) {
        if (persona == null) throw new RuntimeException("Persona is null");
        try {
            Persona savedPersona = personaRepository.save(persona);

            return CompletableFuture.completedFuture(savedPersona);
        } catch (Exception e) {
            throw new RuntimeException("Error: SaveData Message:", e);
        }
    }

    @Async
    @Override
    public CompletableFuture<Persona> getDataById(Integer id) {
        if (id == null) throw new RuntimeException("ID is null");
        try {
            Persona persona = personaRepository.findById(id).orElse(null);
            return CompletableFuture.completedFuture(persona);
        } catch (Exception e) {
            throw new RuntimeException("Error: getDataById Message:", e);
        }
    }

    @Async
    @Override
    public CompletableFuture<List<Persona>> getAllData() {
        try {
            List<Persona> personas = personaRepository.findAll();
            return CompletableFuture.completedFuture(personas);
        } catch (Exception e) {
            throw new RuntimeException("Error: getAllData Message:", e);
        }
    }

    @Override
    public void deleteDataById(Integer id) {
        if (id == null) throw new RuntimeException("ID is null");
        try {
            personaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error: deleteDataById Message:", e);
        }
    }

}