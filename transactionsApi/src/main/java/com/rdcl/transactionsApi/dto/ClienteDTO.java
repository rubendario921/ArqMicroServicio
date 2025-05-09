package com.rdcl.transactionsApi.dto;

import jakarta.persistence.Column;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Integer perId;
    private String perName;
    private String cliPassword;
    private String perIdentification;
    private String perAddress;
    private String perNumberPhone;
    @Column(nullable = false,length = 1)
    private String perGender;
    private Integer perAge;
    private boolean cliStatus;
}