package com.rdcl.Usersapi.controller;

import com.rdcl.Usersapi.dto.ClienteDTO;
import com.rdcl.Usersapi.entity.Cliente;
import com.rdcl.Usersapi.service.IClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
    private final IClienteService clienteService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Cliente>>> getAllClientes() {
        return clienteService.getAllData().thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Cliente>> getClienteById(@PathVariable Integer id) {
        return clienteService.getDataById(id).thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Cliente>> saveCliente(@RequestBody ClienteDTO request) {
        return clienteService.saveData(request).thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Cliente>> updateClienteById(@PathVariable Integer id, @RequestBody ClienteDTO request) {
        return clienteService.updateData(id, request).thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClienteById(@PathVariable Integer id) {
        clienteService.deleteDataById(id);
        return ResponseEntity.noContent().build();
    }

}