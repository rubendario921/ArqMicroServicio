package com.rdcl.transactionsApi.controller;

import com.rdcl.transactionsApi.dto.CuentaDTO;
import com.rdcl.transactionsApi.exception.IsNullOrEmptyException;
import com.rdcl.transactionsApi.service.ICuentaService;
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
@RequestMapping("/api/cuenta")
@Tag(name = "Cuenta", description = "API para la gestión de cuentas")
public class CuentaController {
    private final ICuentaService cuentaService;

    @Operation(summary = "Obtener todas las cuentas", description = "Retorna una lista de todas las cuentas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cuentas encontrada",
                    content = @Content(schema = @Schema(implementation = CuentaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud", content = @Content)
    })
    @GetMapping
    public CompletableFuture<ResponseEntity<List<CuentaDTO>>> getAllCuentas() {
        return cuentaService.getAllData()
                .thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Obtener cuenta por número de cuenta", description = "Retorna una cuenta por su número de cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content)
    })
    @GetMapping("/{accountNumber}")
    public CompletableFuture<ResponseEntity<CuentaDTO>> getCuentaByAccountNumber(@PathVariable String accountNumber) {

        CuentaDTO account = cuentaService.getDataByAccountNumber(accountNumber).join();
        if (account == null) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(account));
    }

    @Operation(summary = "Obtener cuentas por ID de cliente", description = "Retorna todas las cuentas asociadas a un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuentas encontradas"),
            @ApiResponse(responseCode = "404", description = "No se encontraron cuentas", content = @Content)
    })
    @GetMapping("/cliente/{clienteId}")
    public CompletableFuture<ResponseEntity<List<CuentaDTO>>> getCuentasByClienteId(@PathVariable Integer clienteId) {
        List<CuentaDTO> cuentas = cuentaService.getDataByClienteID(clienteId).join();
        if (cuentas.isEmpty()) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(cuentas));
    }


    @Operation(summary = "Crear nueva cuenta", description = "Crea una nueva cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de cuenta inválidos", content = @Content)
    })
    @PostMapping
    public CompletableFuture<ResponseEntity<CuentaDTO>> createCuenta(@RequestBody CuentaDTO cuentaDTO) {
        if (cuentaDTO == null)
            throw new IsNullOrEmptyException("CuentaController", "createCuenta", "Request is null or empty");
        CuentaDTO newCuenta = cuentaService.saveData(cuentaDTO).join();
        if (newCuenta == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(newCuenta));
    }

    @Operation(summary = "Actualizar cuenta", description = "Actualiza una cuenta bancaria existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de cuenta inválidos", content = @Content)
    })
    @PutMapping("/{accountNumber}")
    public CompletableFuture<ResponseEntity<CuentaDTO>> updateCuenta(
            @PathVariable String accountNumber,
            @RequestBody CuentaDTO cuentaDTO) {
        CuentaDTO updatedCuenta = cuentaService.updateData(accountNumber, cuentaDTO).join();
        if (updatedCuenta == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(updatedCuenta));
    }

    @Operation(summary = "Eliminar cuenta", description = "Elimina una cuenta bancaria existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content)
    })
    @DeleteMapping("/{accountNumber}")
    public CompletableFuture<ResponseEntity<Void>> deleteCuenta(@PathVariable String accountNumber) {
        cuentaService.deleteData(accountNumber).join();
        return CompletableFuture.completedFuture(ResponseEntity.noContent().build());
    }
}