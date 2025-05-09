package com.rdcl.transactionsApi.controller;

import com.rdcl.transactionsApi.dto.ReporteDTO;
import com.rdcl.transactionsApi.exception.IsNullOrEmptyException;
import com.rdcl.transactionsApi.service.IReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    public final IReporteService reporteService;

    @GetMapping("/cliente")
    public CompletableFuture<ResponseEntity<?>> generarReporteByClienteId(
            @RequestParam Integer clienteId,
            @RequestParam LocalDate dateStart,
            @RequestParam LocalDate dateEnd) {
        if (clienteId == null || dateStart == null || dateEnd == null)
            throw new IsNullOrEmptyException("ReporteController", "generarReporte", "Data is null or empty");
        List<ReporteDTO> reporteDTOList = reporteService.generateAccountStatement(clienteId, dateStart, dateEnd).join();
        if (reporteDTOList.isEmpty()) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(reporteDTOList));
    }

    @GetMapping("/cuenta")
    public CompletableFuture<ResponseEntity<?>> generarReporteByAccountNumber(
            @RequestParam String accountNumber,
            @RequestParam LocalDate dateStart,
            @RequestParam LocalDate dateEnd) {
        if (accountNumber == null || dateStart == null || dateEnd == null)
            throw new IsNullOrEmptyException("ReporteController", "generarReporte", "Data is null or empty");
        List<ReporteDTO> reporteDTOList = reporteService.generateAccountStatementByAccountNumber(accountNumber, dateStart, dateEnd).join();
        if (reporteDTOList.isEmpty()) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(reporteDTOList));
    }

}