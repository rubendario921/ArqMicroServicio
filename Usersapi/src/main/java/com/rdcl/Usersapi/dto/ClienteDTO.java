package com.rdcl.Usersapi.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ClienteDTO {
    private String cliName;
    private String cliPassword;
    private String cliIdentification;
    private String cliAddress;
    private String cliNumberPhone;
    @Column(nullable = false,length = 1)
    private String cliGender;
    private Integer cliAge;
}