package com.rdcl.transactionsApi.service;

import com.rdcl.transactionsApi.dto.ReporteDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IReporteService {
    CompletableFuture<List<ReporteDTO>> generateAccountStatement(Integer clienteId, LocalDate dateStart, LocalDate dateEnd);
    CompletableFuture<List<ReporteDTO>> generateAccountStatementByAccountNumber(String accountNumber, LocalDate dateStart, LocalDate dateEnd);
}