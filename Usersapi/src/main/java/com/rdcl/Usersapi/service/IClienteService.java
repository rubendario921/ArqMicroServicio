package com.rdcl.Usersapi.service;

import com.rdcl.Usersapi.dto.ClienteDTO;
import com.rdcl.Usersapi.entity.Cliente;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IClienteService {
    CompletableFuture<Cliente> saveData(ClienteDTO clienteReques);

    CompletableFuture<Cliente> updateData(Integer id, ClienteDTO clienteRequest);

    CompletableFuture<Cliente> getDataById(Integer id);

    CompletableFuture<List<Cliente>> getAllData();

    void deleteDataById(Integer id);
}