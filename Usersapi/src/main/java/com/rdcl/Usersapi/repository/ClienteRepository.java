package com.rdcl.Usersapi.repository;

import com.rdcl.Usersapi.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    @Query("SELECT c FROM Cliente c WHERE c.perIdentification = :identification")
    Cliente findBycliIdentification(@Param("identification") String identification);
}