package com.rdcl.transactionsApi.service;

import com.rdcl.transactionsApi.dto.MovimientoDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IMovimientoService {
    CompletableFuture<List<MovimientoDTO>> getAllData();

    CompletableFuture<MovimientoDTO> getDataById(Integer id);

    CompletableFuture<List<MovimientoDTO>> getDataByAccountNumber(String accountNumber);

    CompletableFuture<List<MovimientoDTO>> getByClienteIdAndFechaBetween(Integer clienteId, LocalDate fechaInicio, LocalDate fechaFin);

    CompletableFuture<MovimientoDTO> saveDate(MovimientoDTO movimientoDTO);

    CompletableFuture<MovimientoDTO> updateDate(Integer id, MovimientoDTO movimientoDTO);

    CompletableFuture<Void> deleteDate(Integer id);


}