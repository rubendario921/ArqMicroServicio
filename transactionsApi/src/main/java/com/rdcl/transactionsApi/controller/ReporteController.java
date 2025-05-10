package com.rdcl.transactionsApi.controller;

import com.rdcl.transactionsApi.dto.ReporteDTO;
import com.rdcl.transactionsApi.exception.IsNullOrEmptyException;
import com.rdcl.transactionsApi.service.IReporteService;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;


import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "API para gestión de reportes de transacciones")
public class ReporteController {
    public final IReporteService reporteService;


    @Operation(summary = "Generar reporte por cliente",
            description = "Genera un reporte de transacciones para un cliente específico en un rango de fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @GetMapping("/cliente")
    public CompletableFuture<ResponseEntity<?>> generarReporteByClienteId(
            @Parameter(description = "ID del cliente", required = true)
            @RequestParam Integer clienteId,
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate dateStart,
            @Parameter(description = "Fecha final (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate dateEnd) {
        if (clienteId == null || dateStart == null || dateEnd == null)
            throw new IsNullOrEmptyException("ReporteController", "generarReporte", "Data is null or empty");
        List<ReporteDTO> reporteDTOList = reporteService.generateAccountStatement(clienteId, dateStart, dateEnd).join();
        if (reporteDTOList.isEmpty()) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(reporteDTOList));
    }

    @Operation(summary = "Generar reporte por número de cuenta",
            description = "Genera un reporte de transacciones para una cuenta específica en un rango de fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content)
    })
    @GetMapping("/cuenta")
    public CompletableFuture<ResponseEntity<?>> generarReporteByAccountNumber(
            @Parameter(description = "Número de cuenta", required = true)
            @RequestParam String accountNumber,
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate dateStart,
            @Parameter(description = "Fecha final (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate dateEnd) {
        if (accountNumber == null || dateStart == null || dateEnd == null)
            throw new IsNullOrEmptyException("ReporteController", "generarReporte", "Data is null or empty");
        List<ReporteDTO> reporteDTOList = reporteService.generateAccountStatementByAccountNumber(accountNumber, dateStart, dateEnd).join();
        if (reporteDTOList.isEmpty()) return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        return CompletableFuture.completedFuture(ResponseEntity.ok(reporteDTOList));
    }

}