package com.rdcl.transactionsApi.service;

import com.rdcl.transactionsApi.clientApi.UserApiClient;
import com.rdcl.transactionsApi.dto.ClienteDTO;
import com.rdcl.transactionsApi.dto.ReporteDTO;
import com.rdcl.transactionsApi.entity.Cuenta;
import com.rdcl.transactionsApi.entity.Movimiento;
import com.rdcl.transactionsApi.exception.IsNullOrEmptyException;
import com.rdcl.transactionsApi.exception.ResourceNotFoundException;
import com.rdcl.transactionsApi.repository.CuentaRepository;
import com.rdcl.transactionsApi.repository.MovimientoRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class ReporteService implements IReporteService {
    private final UserApiClient userApiClient;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public ReporteService( UserApiClient userApiClient, CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {

        this.userApiClient = userApiClient;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Async
    @Override
    public CompletableFuture<List<ReporteDTO>> generateAccountStatement(Integer clienteId, LocalDate dateStart, LocalDate dateEnd) {
        if (clienteId == null || dateStart == null || dateEnd == null)
            throw new IsNullOrEmptyException("ReporteService", "generateAccountStatement", "The data is null or empty");
        try {
            ClienteDTO clienteDTO = userApiClient.getClienteById(clienteId);
            if (clienteDTO == null) throw new ResourceNotFoundException("generateAccountStatement", "Data not found");

            List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
            if (cuentas.isEmpty()) throw new ResourceNotFoundException("generateAccountStatement", "Data not found");

            List<ReporteDTO> reporteItems = new ArrayList<>();
            LocalDateTime start = dateStart.atStartOfDay();
            LocalDateTime end = dateEnd.atTime(LocalTime.MAX);

            for (Cuenta cuenta : cuentas) {
                List<Movimiento> movimientos = movimientoRepository.findByCuentaNumeroCuentaAndFechaBetween(cuenta.getAccountNumber(), start, end);
                if (movimientos.isEmpty()) {
                    ReporteDTO item = new ReporteDTO();
                    item.setDate(LocalDate.now());
                    item.setCustomer(clienteDTO.getPerName());
                    item.setAccountNumber(cuenta.getAccountNumber());
                    item.setAccountType(cuenta.getAccountType());
                    item.setInitialValue(cuenta.getInitialValue());
                    item.setStatus(cuenta.getStatus());
                    item.setMovement(null);
                    item.setAvailableValue(cuenta.getAvailableValue());
                    reporteItems.add(item);
                } else {
                    for (Movimiento movimiento : movimientos) {
                        ReporteDTO item = new ReporteDTO();
                        item.setDate(movimiento.getDate().toLocalDate());
                        item.setCustomer(clienteDTO.getPerName());
                        item.setAccountNumber(cuenta.getAccountNumber());
                        item.setAccountType(cuenta.getAccountType());
                        item.setInitialValue(cuenta.getInitialValue());
                        item.setStatus(cuenta.getStatus());
                        item.setMovement(movimiento.getValue());
                        item.setAvailableValue(movimiento.getBalance());

                        reporteItems.add(item);
                    }
                }
            }
            return CompletableFuture.completedFuture(reporteItems);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<List<ReporteDTO>> generateAccountStatementByAccountNumber(String accountNumber, LocalDate dateStart, LocalDate dateEnd) {
        if (accountNumber == null || dateStart == null || dateEnd == null)
            throw new IsNullOrEmptyException("ReporteService", "generateAccountStatement", "The data is null or empty");
        try {
            Cuenta cuenta = cuentaRepository.findById(accountNumber).orElseThrow(() -> new ResourceNotFoundException("generateAccountStatementByAccountNumber", "Account Number not found"));

            ClienteDTO clienteDTO = userApiClient.getClienteById(cuenta.getClienteId());
            if (clienteDTO == null)
                throw new ResourceNotFoundException("generateAccountStatementByAccountNumber", "Data not found");

            List<ReporteDTO> reporteItems = new ArrayList<>();
            LocalDateTime start = dateStart.atStartOfDay();
            LocalDateTime end = dateEnd.atTime(LocalTime.MAX);

            List<Movimiento> movimientos = movimientoRepository.findByCuentaNumeroCuentaAndFechaBetween(cuenta.getAccountNumber(), start, end);
            if (movimientos.isEmpty()) {
                ReporteDTO item = new ReporteDTO();
                item.setDate(LocalDate.now());
                item.setCustomer(clienteDTO.getPerName());
                item.setAccountNumber(cuenta.getAccountNumber());
                item.setAccountType(cuenta.getAccountType());
                item.setInitialValue(cuenta.getInitialValue());
                item.setStatus(cuenta.getStatus());
                item.setMovement(null);
                item.setAvailableValue(cuenta.getAvailableValue());

                reporteItems.add(item);
            } else {
                for (Movimiento movimiento : movimientos) {
                    ReporteDTO item = new ReporteDTO();
                    item.setDate(movimiento.getDate().toLocalDate());
                    item.setCustomer(clienteDTO.getPerName());
                    item.setAccountNumber(cuenta.getAccountNumber());
                    item.setAccountType(cuenta.getAccountType());
                    item.setInitialValue(cuenta.getInitialValue());
                    item.setStatus(cuenta.getStatus());
                    item.setMovement(movimiento.getValue());
                    item.setAvailableValue(movimiento.getBalance());

                    reporteItems.add(item);
                }
            }
            return CompletableFuture.completedFuture(reporteItems);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}