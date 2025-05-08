package com.rdcl.Usersapi.service;

import com.rdcl.Usersapi.entity.Persona;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IPersonaService {
    CompletableFuture<Persona> saveData(Persona persona);

    CompletableFuture<Persona> getDataById(Integer id);

    CompletableFuture<List<Persona>> getAllData();

    void deleteDataById(Integer id);
}