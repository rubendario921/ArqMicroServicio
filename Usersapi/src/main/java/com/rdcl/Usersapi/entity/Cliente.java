package com.rdcl.Usersapi.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "per_id")
public class Cliente extends Persona {

    @Column(name = "cli_password", nullable = false)
    private String cliPassword;

    @Column(nullable = false)
    private Boolean cliStatus;
}