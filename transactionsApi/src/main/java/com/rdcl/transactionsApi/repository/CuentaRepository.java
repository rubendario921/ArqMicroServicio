package com.rdcl.transactionsApi.repository;

import com.rdcl.transactionsApi.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaRepository extends JpaRepository <Cuenta,String>{
    List<Cuenta> findByClienteId(Integer clienteId);
    boolean existsByClienteId(Integer clienteId);
}