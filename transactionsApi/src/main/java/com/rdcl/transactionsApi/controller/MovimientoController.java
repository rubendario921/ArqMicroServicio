package com.rdcl.transactionsApi.controller;

import com.rdcl.transactionsApi.dto.MovimientoDTO;
import com.rdcl.transactionsApi.repository.CuentaRepository;
import com.rdcl.transactionsApi.service.IMovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movimiento")
public class MovimientoController {

    private final IMovimientoService movimientoService;


    @GetMapping
    public CompletableFuture<ResponseEntity<List<MovimientoDTO>>> getAllMovimientos() {
        return movimientoService.getAllData().thenApply(ResponseEntity::ok).exceptionally(throwable -> {
            return ResponseEntity.internalServerError().build();
        });
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<MovimientoDTO>> getMovimientoById(@PathVariable Integer id) {
        if(id==null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        MovimientoDTO movimiento = movimientoService.getDataById(id).join();
        if (movimiento == null) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(movimiento));
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<MovimientoDTO>> createMovimiento(@RequestBody MovimientoDTO movimientoDTO) {
        if(movimientoDTO.getAccountNumber()==null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        MovimientoDTO newMovimiento = movimientoService.saveDate(movimientoDTO).join();
        if (newMovimiento == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(newMovimiento));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<MovimientoDTO>> updateMovimiento(
            @PathVariable Integer id,
            @RequestBody MovimientoDTO movimientoDTO) {
        if(id==null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        MovimientoDTO updatedMovimiento = movimientoService.updateDate(id, movimientoDTO).join();
        if (updatedMovimiento == null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(updatedMovimiento));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteMovimiento(@PathVariable Integer id) {
        if(id==null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        movimientoService.deleteDate(id).join();
        return CompletableFuture.completedFuture(ResponseEntity.noContent().build());
    }

    @GetMapping("/cuenta/{accountNumber}")
    public CompletableFuture<ResponseEntity<List<MovimientoDTO>>> getMovimientosByCuenta(@PathVariable String accountNumber) {
        if(accountNumber==null) return CompletableFuture.completedFuture(ResponseEntity.badRequest().build());
        List<MovimientoDTO> movimientos = movimientoService.getDataByAccountNumber(accountNumber).join();
        if (movimientos.isEmpty()) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(movimientos));
    }



}