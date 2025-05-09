package com.rdcl.transactionsApi.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDTO {
    private Integer movId;
    private LocalDateTime date;
    private String movementType;
    private BigDecimal value;
    private BigDecimal balance;
    private String accountNumber;
    private String accountType;
    private Integer clienteId;
    private String name;
}