
//output from backend → frontend
package com.zeta.Digital_Fixed_Deposit_System.dto;

import com.zeta.Digital_Fixed_Deposit_System.entity.FDStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedDepositResponse {
    private Long fixedDepositId;
    private Long userId;
    private BigDecimal amount;
    private Integer tenureMonths;
    @Column(name = "scheme")
    private String scheme;


    private BigDecimal interestRate;
    private LocalDate startDate;
    private LocalDate maturityDate;
    private FDStatus status;

    private BigDecimal accruedInterest;
    private LocalDateTime createdAt;
}