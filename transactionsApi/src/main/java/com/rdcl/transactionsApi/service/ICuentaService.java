package com.rdcl.transactionsApi.service;

import com.rdcl.transactionsApi.dto.CuentaDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ICuentaService {
    CompletableFuture<List<CuentaDTO>> getAllData();

    CompletableFuture<CuentaDTO> getDataByAccountNumber(String accountNumber);

    CompletableFuture<List<CuentaDTO>> getDataByClienteID(Integer clienteId);

    CompletableFuture<CuentaDTO> saveData(CuentaDTO request);

    CompletableFuture<CuentaDTO> updateData(String accountNumber, CuentaDTO request);

    CompletableFuture<Void> deleteData(String accountNumber);

    CompletableFuture<Boolean> existsByClientId(Integer clienteId);
}