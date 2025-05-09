package com.rdcl.transactionsApi.controller;

import com.rdcl.transactionsApi.dto.CuentaDTO;
import com.rdcl.transactionsApi.exception.IsNullOrEmptyException;
import com.rdcl.transactionsApi.service.ICuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {
    private final ICuentaService cuentaService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<CuentaDTO>>> getAllCuentas() {
        return cuentaService.getAllData()
                .thenApply(ResponseEntity::ok).exceptionally(ex -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{accountNumber}")
    public CompletableFuture<ResponseEntity<CuentaDTO>> getCuentaByAccountNumber(@PathVariable String accountNumber) {

        CuentaDTO account = cuentaService.getDataByAccountNumber(accountNumber).join();
        if (account == null) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(account));
    }


    @GetMapping("/cliente/{clienteId}")
    public CompletableFuture<ResponseEntity<List<CuentaDTO>>> getCuentasByClienteId(@PathVariable Integer clienteId) {
        List<CuentaDTO> cuentas = cuentaService.getDataByClienteID(clienteId).join();
        if (cuentas.isEmpty()) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(cuentas));
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<CuentaDTO>> createCuenta(@RequestBody CuentaDTO cuentaDTO) {
        if (cuentaDTO == null)
            throw new IsNullOrEmptyException("CuentaController", "createCuenta", "Request is null or empty");
        CuentaDTO newCuenta = cuentaService.saveData(cuentaDTO).join();
        if (newCuenta == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(newCuenta));
    }

    @PutMapping("/{accountNumber}")
    public CompletableFuture<ResponseEntity<CuentaDTO>> updateCuenta(
            @PathVariable String accountNumber,
            @RequestBody CuentaDTO cuentaDTO) {
        CuentaDTO updatedCuenta = cuentaService.updateData(accountNumber, cuentaDTO).join();
        if (updatedCuenta == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(updatedCuenta));
    }

    @DeleteMapping("/{accountNumber}")
    public CompletableFuture<ResponseEntity<Void>> deleteCuenta(@PathVariable String accountNumber) {
        cuentaService.deleteData(accountNumber).join();
        return CompletableFuture.completedFuture(ResponseEntity.noContent().build());
    }
}