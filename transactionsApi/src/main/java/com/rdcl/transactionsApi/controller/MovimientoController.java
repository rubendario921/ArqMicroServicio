package com.rdcl.transactionsApi.controller;

import com.rdcl.transactionsApi.dto.MovimientoDTO;
import com.rdcl.transactionsApi.service.IMovimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movimiento")
@Tag(name = "Movimientos", description = "API para la gestión de movimientos en cuentas bancarias")
public class MovimientoController {

    private final IMovimientoService movimientoService;

    @Operation(summary = "Obtener todos los movimientos", description = "Devuelve una lista con todos los movimientos registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados",
                    content = @Content(schema = @Schema(implementation = MovimientoDTO.class))
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping
    public CompletableFuture<ResponseEntity<List<MovimientoDTO>>> getAllMovimientos() {
        return movimientoService.getAllData().thenApply(ResponseEntity::ok).exceptionally(throwable -> {
            return ResponseEntity.internalServerError().build();
        });
    }

    @Operation(summary = "Obtener los movimientos por ID", description = "Busca y devuelve un movimiento según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados",
                    content = @Content(schema = @Schema(implementation = MovimientoDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Movimientos no encontrados",
                    content = @Content
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<MovimientoDTO>> getMovimientoById(@PathVariable Integer id) {
        if (id == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        MovimientoDTO movimiento = movimientoService.getDataById(id).join();
        if (movimiento == null) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(movimiento));
    }

    @Operation(summary = "Cerar un nuevo movimiento", description = "Registra un nuevo movimiento en la cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento creado exitosamente",
                    content = @Content(schema = @Schema(implementation = MovimientoDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o número de cuenta no especificado",
                    content = @Content
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping
    public CompletableFuture<ResponseEntity<MovimientoDTO>> createMovimiento(@RequestBody MovimientoDTO movimientoDTO) {
        if (movimientoDTO.getAccountNumber() == null)
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        MovimientoDTO newMovimiento = movimientoService.saveDate(movimientoDTO).join();
        if (newMovimiento == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(newMovimiento));
    }

    @Operation(summary = "Actualizar un movimiento existente", description = "Actualiza los datos de un movimiento según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = MovimientoDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "ID inválido o datos incorrectos",
                    content = @Content
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<MovimientoDTO>> updateMovimiento(
            @PathVariable Integer id,
            @RequestBody MovimientoDTO movimientoDTO) {
        if (id == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        MovimientoDTO updatedMovimiento = movimientoService.updateDate(id, movimientoDTO).join();
        if (updatedMovimiento == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(updatedMovimiento));
    }

    @Operation(summary = "Eliminar un movimiento", description = "Elimina un movimiento según su ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204", description = "Movimiento eliminado exitosamente",
                    content = @Content
            ),
            @ApiResponse(responseCode = "400", description = "ID inválido",
                    content = @Content
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteMovimiento(@PathVariable Integer id) {
        if (id == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        movimientoService.deleteDate(id).join();
        return CompletableFuture.completedFuture(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Obtener movimientos por número de cuenta", description = "Devuelve todos los movimientos asociados a un número de cuenta específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados para la cuenta",
                    content = @Content(schema = @Schema(implementation = MovimientoDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "No se encontraron movimientos para esa cuenta",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400", description = "Número de cuenta inválido",
                    content = @Content
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/cuenta/{accountNumber}")
    public CompletableFuture<ResponseEntity<List<MovimientoDTO>>> getMovimientosByCuenta(@PathVariable String accountNumber) {
        if (accountNumber == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        List<MovimientoDTO> movimientos = movimientoService.getDataByAccountNumber(accountNumber).join();
        if (movimientos.isEmpty()) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(movimientos));
    }
}