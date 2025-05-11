package com.rdcl.Usersapi.controller;

import com.rdcl.Usersapi.dto.ClienteDTO;
import com.rdcl.Usersapi.entity.Cliente;
import com.rdcl.Usersapi.service.IClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cliente")
@Tag(name = "Cliente", description = "API para la gestión de clientes")
public class ClienteController {
    private final IClienteService clienteService;

    @Operation(summary = "Obtener todos los clientes", description = "Obtiene todos los datos de clientes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron clientes", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public CompletableFuture<ResponseEntity<List<Cliente>>> getAllClientes() {
        return clienteService.getAllData().thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity.notFound().build());
    }


    @Operation(summary = "Obtener datos por ID", description = "Obtiene los datos registrados del cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "No se encontro el  cliente por el ID", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Cliente>> getClienteById(@PathVariable Integer id) {
        return clienteService.getDataById(id).thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un cliente", description = "Crea un nuevo cliente para el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OK", content = @Content(schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public CompletableFuture<ResponseEntity<Cliente>> saveCliente(@RequestBody ClienteDTO request) {
        return clienteService.saveData(request).thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @Operation(summary = "Actualizar datos de un cliente", description = "Actualiza los datos de un cliente registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Cliente>> updateClienteById(@PathVariable Integer id, @RequestBody ClienteDTO request) {
        return clienteService.updateData(id, request).thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClienteById(@PathVariable Integer id) {
        clienteService.deleteDataById(id);
        return ResponseEntity.noContent().build();
    }

}