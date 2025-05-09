package com.rdcl.transactionsApi.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDTO {
    private LocalDate date;
    private String customer;
    private String accountNumber;
    private String accountType;
    private BigDecimal initialValue;
    private Boolean status;
    private BigDecimal movement;
    private BigDecimal availableValue;
}