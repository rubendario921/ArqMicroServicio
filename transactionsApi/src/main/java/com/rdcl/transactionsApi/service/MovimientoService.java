package com.rdcl.transactionsApi.service;

import com.rdcl.transactionsApi.clientApi.UserApiClient;
import com.rdcl.transactionsApi.dto.ClienteDTO;
import com.rdcl.transactionsApi.dto.MovimientoDTO;
import com.rdcl.transactionsApi.entity.Cuenta;
import com.rdcl.transactionsApi.entity.Movimiento;
import com.rdcl.transactionsApi.exception.IsNullOrEmptyException;
import com.rdcl.transactionsApi.exception.ResourceNotFoundException;
import com.rdcl.transactionsApi.exception.UnavailableValue;
import com.rdcl.transactionsApi.repository.CuentaRepository;
import com.rdcl.transactionsApi.repository.MovimientoRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class MovimientoService implements IMovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final UserApiClient userApiClient;
    private final CuentaRepository cuentaRepository;

    public MovimientoService(MovimientoRepository movimientoRepository, UserApiClient userApiClient, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.userApiClient = userApiClient;
        this.cuentaRepository = cuentaRepository;
    }


    @Async
    @Override
    public CompletableFuture<List<MovimientoDTO>> getAllData() {
        try {
            List<Movimiento> movimientos = movimientoRepository.findAll();
            List<MovimientoDTO> movimientoDTOs = movimientos.stream()
                    .map(this::mapToDTO)
                    .toList();
            return CompletableFuture.completedFuture(movimientoDTOs);
        } catch (Exception e) {
            throw new RuntimeException("Error getAllData", e);
        }

    }


    @Async
    @Override
    public CompletableFuture<MovimientoDTO> getDataById(Integer id) {
        if (id == null) throw new IsNullOrEmptyException("MovimientoService", "getDataById", "Id is null");
        try {
            Movimiento movimiento = movimientoRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimiento not found"));
            return CompletableFuture.completedFuture(mapToDTO(movimiento));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<List<MovimientoDTO>> getDataByAccountNumber(String accountNumber) {
        if (accountNumber == null)
            throw new IsNullOrEmptyException("MovimientoService", "getDataByAccountNumber", "Account Number is null");
        try {
            List<Movimiento> movimientos = movimientoRepository.findByCuenta_AccountNumber(accountNumber);
            List<MovimientoDTO> movimientoDTOs = movimientos.stream()
                    .map(this::mapToDTO)
                    .toList();
            return CompletableFuture.completedFuture(movimientoDTOs);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    @Async
    @Override
    public CompletableFuture<List<MovimientoDTO>> getByClienteIdAndFechaBetween(Integer clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (clienteId == null | fechaInicio == null | fechaFin == null)
            throw new IsNullOrEmptyException("MovimientoService", "getByClienteIdAndFechaBetween", "The data is null or empty");
        try {
            ClienteDTO clienteDTO = userApiClient.getClienteById(clienteId);
            if (clienteDTO == null) throw new ResourceNotFoundException("userApiClient", "The client does not exist");

            LocalDateTime dateStart = fechaInicio.atStartOfDay();
            LocalDateTime dateEnd = fechaFin.atTime(LocalTime.MAX);

            List<Movimiento> movimientos = movimientoRepository.findByClienteIdAndFechaBetween(clienteId, dateStart, dateEnd);
            List<MovimientoDTO> movimientoDTOs = movimientos.stream()
                    .map(this::mapToDTO)
                    .toList();
            return CompletableFuture.completedFuture(movimientoDTOs);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<MovimientoDTO> saveDate(MovimientoDTO movimientoDTO) {
        if (movimientoDTO == null)
            throw new IsNullOrEmptyException("MovimientoService", "saveDate", "The data is null or empty");
        try {
            Cuenta cuenta = cuentaRepository.findById(movimientoDTO.getAccountNumber()).orElseThrow(() -> new ResourceNotFoundException("saveDate", "Account Number not found"));

            //TypeMovement
            if (movimientoDTO.getValue().compareTo(BigDecimal.ZERO) < 0) {
                //Withdrawal
                BigDecimal newBalance = cuenta.getAvailableValue().add(movimientoDTO.getValue());
                if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                    throw new UnavailableValue(cuenta.getAccountNumber(), "Insufficient balance");
                }
                cuenta.setAvailableValue(newBalance);
                movimientoDTO.setMovementType("Withdrawal");
            } else {
                //Deposit
                BigDecimal newBalance = cuenta.getAvailableValue().add(movimientoDTO.getValue());
                cuenta.setAvailableValue(newBalance);
                movimientoDTO.setMovementType("Deposit");
            }

            //Update Accounts
            cuentaRepository.save(cuenta);

            //New Movement
            Movimiento newData = new Movimiento();
            newData.setDate(LocalDateTime.now());
            newData.setMovementType(movimientoDTO.getMovementType());
            newData.setValue(movimientoDTO.getValue());
            newData.setBalance(cuenta.getAvailableValue());
            newData.setCuenta(cuenta);

            Movimiento savedMovimiento = movimientoRepository.save(newData);
            return CompletableFuture.completedFuture(mapToDTO(savedMovimiento));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<MovimientoDTO> updateDate(Integer id, MovimientoDTO movimientoDTO) {
        if (id == null || movimientoDTO == null)
            throw new IsNullOrEmptyException("MovimientoService", "updateDate", "The data is null or empty");

        try {
            Movimiento movimiento = movimientoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("updateDate", "Movimiento not found"));
            if (movimientoDTO.getDate() != null) movimiento.setDate(movimientoDTO.getDate());

            Movimiento updatedMovimiento = movimientoRepository.save(movimiento);
            return CompletableFuture.completedFuture(mapToDTO(updatedMovimiento));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<Void> deleteDate(Integer id) {
        if (id == null)
            throw new IsNullOrEmptyException("MovimientoService", "deleteDate", "The data is null or empty");
        try {
            Movimiento movimiento = movimientoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("deleteDate", "Movimiento not found"));

            //Dont generate movements for deleted accounts
            Cuenta cuenta = movimiento.getCuenta();
            //Deposit
            BigDecimal valueAdjustment = movimiento.getValue().negate();

            //Update balance
            BigDecimal newBalance = cuenta.getAvailableValue().add(valueAdjustment);

            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new UnavailableValue(cuenta.getAccountNumber(), "Insufficient balance");
            }
            cuenta.setAvailableValue(newBalance);
            cuentaRepository.save(cuenta);
            movimientoRepository.delete(movimiento);
            return CompletableFuture.completedFuture(null);


        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    // Map of entity a dto
    private MovimientoDTO mapToDTO(Movimiento entity) {
        if (entity == null) throw new IsNullOrEmptyException("MovimientoService", "mapToDTO", "Entity is null");
        try {
            MovimientoDTO dto = new MovimientoDTO();
            dto.setMovId(entity.getMovId());
            dto.setDate(entity.getDate());
            dto.setMovementType(entity.getMovementType());
            dto.setValue(entity.getValue());
            dto.setBalance(entity.getBalance());
            dto.setAccountNumber(entity.getCuenta().getAccountNumber());
            dto.setAccountType(entity.getCuenta().getAccountType());
            dto.setClienteId(entity.getCuenta().getClienteId());

            // Get name client
            ClienteDTO clienteDTO = userApiClient.getClienteById(entity.getCuenta().getClienteId());
            dto.setName(clienteDTO != null ? clienteDTO.getPerName() : "Client not found");
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}