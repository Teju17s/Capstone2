package com.zeta.Digital_Fixed_Deposit_System.dto;

import com.zeta.Digital_Fixed_Deposit_System.entity.FDStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for returning Fixed Deposit (FD) details to the client.
 * <p>
 * This DTO represents the response sent from the backend to the frontend
 * after booking or retrieving FD information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedDepositResponse {

    /**
     * Unique identifier for the Fixed Deposit.
     */
    private Long fixedDepositId;

    /**
     * ID of the user associated with this Fixed Deposit.
     */
    private Long userId;

    /**
     * The deposited amount.
     */
    private BigDecimal amount;

    /**
     * Duration of the Fixed Deposit in months.
     */
    private Integer tenureMonths;

    /**
     * The FD scheme name selected by the user.
     */
    private String scheme;

    /**
     * Interest rate applied to this Fixed Deposit.
     */
    private BigDecimal interestRate;

    /**
     * Date when the Fixed Deposit starts.
     */
    private LocalDate startDate;

    /**
     * Date when the Fixed Deposit matures.
     */
    private LocalDate maturityDate;

    /**
     * Current status of the Fixed Deposit (e.g., ACTIVE, CLOSED, WITHDRAWN).
     */
    private FDStatus status;

    /**
     * Total interest accrued till the current date.
     */
    private BigDecimal accruedInterest;

    /**
     * Timestamp when the Fixed Deposit record was created.
     */
    private LocalDateTime createdAt;
}





















//
////output from backend → frontend
//package com.zeta.Digital_Fixed_Deposit_System.dto;
//
//import com.zeta.Digital_Fixed_Deposit_System.entity.FDStatus;
//import jakarta.persistence.Column;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class FixedDepositResponse {
//    private Long fixedDepositId;
//    private Long userId;
//    private BigDecimal amount;
//    private Integer tenureMonths;
//    @Column(name = "scheme")
//    private String scheme;
//
//
//    private BigDecimal interestRate;
//    private LocalDate startDate;
//    private LocalDate maturityDate;
//    private FDStatus status;
//
//    private BigDecimal accruedInterest;
//    private LocalDateTime createdAt;
//}
//
