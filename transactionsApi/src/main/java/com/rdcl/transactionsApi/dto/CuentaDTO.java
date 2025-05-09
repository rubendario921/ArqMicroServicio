package com.rdcl.transactionsApi.dto;


import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {
    private String accountNumber;
    private String accountType;
    private BigDecimal initialValue;
    private BigDecimal availableValue;
    private Boolean status;
    private Integer clienteId;
    private String name;
}