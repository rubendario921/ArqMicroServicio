package com.rdcl.transactionsApi.repository;

import com.rdcl.transactionsApi.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
    List<Movimiento> findByCuenta_AccountNumber(String accountNumber);


    @Query("SELECT m FROM Movimiento m JOIN m.cuenta c WHERE c.clienteId = :clienteId AND m.date BETWEEN :fechaInicio AND :fechaFin ORDER BY m.date DESC")
    List<Movimiento> findByClienteIdAndFechaBetween(
            @Param("clienteId") Integer clienteId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    LocalDateTime date(LocalDateTime date);

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.accountNumber = :numeroCuenta AND m.date BETWEEN :fechaInicio AND :fechaFin ORDER BY m.date DESC")
    List<Movimiento> findByCuentaNumeroCuentaAndFechaBetween(
            @Param("numeroCuenta") String numeroCuenta,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);
}