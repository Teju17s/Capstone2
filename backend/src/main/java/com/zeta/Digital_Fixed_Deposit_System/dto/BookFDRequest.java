
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














//package com.zeta.Digital_Fixed_Deposit_System.dto;
//
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
///**
// * DTO for booking a new Fixed Deposit.
// * Contains all fields required to create an FD.
// * Includes validation to ensure data integrity.
// */
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class BookFDRequest {
//
//    @NotNull(message = "User ID is required")
//    private Long userId;
//
//    @NotNull(message = "Amount is required")
//    @Min(value = 1000, message = "Minimum deposit amount is 1000")
//    private BigDecimal amount;
//
//    @NotNull(message = "Scheme is required")
//    private String scheme;
//
//    @NotNull(message = "Tenure (in months) is required")
//    private Integer tenureMonths;
//
//    @NotNull(message = "Interest rate is required")
//    private BigDecimal interestRate;
//
//    @NotNull(message = "Start date is required")
//    private LocalDate startDate;
//
//    private LocalDate maturityDate;
//}
