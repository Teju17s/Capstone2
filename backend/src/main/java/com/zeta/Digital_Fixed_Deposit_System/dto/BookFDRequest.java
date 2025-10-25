
//for client===input from frontend → backend → database
package com.zeta.Digital_Fixed_Deposit_System.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFDRequest {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String scheme;
    private Integer tenureMonths;
    private BigDecimal interestRate;
    private LocalDate startDate;
    private LocalDate maturityDate;
}