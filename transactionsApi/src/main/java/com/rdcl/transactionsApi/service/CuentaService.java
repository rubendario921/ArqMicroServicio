package com.rdcl.transactionsApi.service;

import com.rdcl.transactionsApi.clientApi.UserApiClient;
import com.rdcl.transactionsApi.dto.ClienteDTO;
import com.rdcl.transactionsApi.dto.CuentaDTO;
import com.rdcl.transactionsApi.entity.Cuenta;
import com.rdcl.transactionsApi.exception.IsNullOrEmptyException;
import com.rdcl.transactionsApi.exception.ResourceNotFoundException;
import com.rdcl.transactionsApi.repository.CuentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class CuentaService implements ICuentaService {
    private final CuentaRepository cuentaRepository;
    private final UserApiClient userApiClient;

    public CuentaService(CuentaRepository cuentaRepository, UserApiClient userApiClient) {
        this.cuentaRepository = cuentaRepository;
        this.userApiClient = userApiClient;
    }

    @Async
    @Override
    public CompletableFuture<List<CuentaDTO>> getAllData() {
        try {
            List<Cuenta> cuentas = cuentaRepository.findAll();
            List<CuentaDTO> cuentaDTOs = cuentas.stream()
                    .map(this::mapToDTO)
                    .toList();
            return CompletableFuture.completedFuture(cuentaDTOs);
        } catch (Exception e) {
            throw new RuntimeException("Error getAllData", e);
        }
    }

    @Async
    @Override
    public CompletableFuture<CuentaDTO> getDataByAccountNumber(String accountNumber) {
        if (accountNumber.isEmpty()) throw new IsNullOrEmptyException("CuentaService", "getDataByAccountNumber", "Account Number is empty");
        try {
            Cuenta cuenta = cuentaRepository.findById(accountNumber).orElseThrow(() -> new ResourceNotFoundException("getDataByAccountNumber", "Account Number not found"));
            return CompletableFuture.completedFuture(mapToDTO(cuenta));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<List<CuentaDTO>> getDataByClienteID(Integer clienteId) {
        if (clienteId == null)
            throw new IsNullOrEmptyException("CuentaService", "getDataByClienteID", "Client Id is null");
        try {
            List<Cuenta> cuenta = cuentaRepository.findByClienteId(clienteId);
            List<CuentaDTO> cuentaDTOs = cuenta.stream()
                    .map(this::mapToDTO)
                    .toList();
            return CompletableFuture.completedFuture(cuentaDTOs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<CuentaDTO> saveData(CuentaDTO request) {
        if (request == null)
            throw new IsNullOrEmptyException("CuentaService", "saveData", "Request is null");
        try {
            Cuenta cuenta = mapToEntity(request);
            cuenta.setAvailableValue(cuenta.getInitialValue());

            Cuenta savedCuenta = cuentaRepository.save(cuenta);
            return CompletableFuture.completedFuture(mapToDTO(savedCuenta));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<CuentaDTO> updateData(String accountNumber, CuentaDTO request) {
        if (accountNumber == null || request == null)
            throw new IsNullOrEmptyException("CuentaService", "updateData", "Account Number or Request is null");
        try {
            Cuenta cuenta = cuentaRepository.findById(accountNumber).orElseThrow(() -> new ResourceNotFoundException("updateData", "Account Number not found"));

            cuenta.setAccountType(request.getAccountType());
            cuenta.setStatus(request.getStatus());

            Cuenta updatedCuenta = cuentaRepository.save(cuenta);
            return CompletableFuture.completedFuture(mapToDTO(updatedCuenta));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<Void> deleteData(String accountNumber) {
        if (accountNumber == null)
            throw new IsNullOrEmptyException("CuentaService", "deleteData", "Account Number is null");
        try {
            Cuenta cuenta = cuentaRepository.findById(accountNumber).orElseThrow(() -> new ResourceNotFoundException("deleteData", "Account Number not found"));
            cuentaRepository.delete(cuenta);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<Boolean> existsByClientId(Integer clienteId) {
        if (clienteId == null) throw new IsNullOrEmptyException("CuentaService", "deleteData", "Client ID is null");
        try {
            Boolean result = cuentaRepository.existsByClienteId(clienteId);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Map of Object
    private CuentaDTO mapToDTO(Cuenta entity) {
        if (entity == null) throw new RuntimeException("Entity is null");
        try {
            CuentaDTO dto = new CuentaDTO();
            dto.setAccountNumber(entity.getAccountNumber());
            dto.setAccountType(entity.getAccountType());
            dto.setInitialValue(entity.getInitialValue());
            dto.setAvailableValue(entity.getAvailableValue());
            dto.setStatus(entity.getStatus());
            dto.setClienteId(entity.getClienteId());

            ClienteDTO clienteDTO = userApiClient.getClienteById(entity.getClienteId());
            dto.setName((clienteDTO != null) ? clienteDTO.getPerName() : null);
            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Error mapToDTO Details:{}", e);
        }
    }

    private Cuenta mapToEntity(CuentaDTO dto) {
        if (dto == null) throw new RuntimeException("DTO is null");
        try {
            Cuenta entity = new Cuenta();
            entity.setAccountNumber(dto.getAccountNumber());
            entity.setAccountType(dto.getAccountType());
            entity.setInitialValue(dto.getInitialValue());
            entity.setAvailableValue(dto.getInitialValue());
            entity.setStatus(dto.getStatus());
            entity.setClienteId(dto.getClienteId());
            return entity;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error mapToEntity Details:{}", e);
        }
    }
}