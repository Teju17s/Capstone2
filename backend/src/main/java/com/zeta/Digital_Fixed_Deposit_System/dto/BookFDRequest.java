package com.zeta.Digital_Fixed_Deposit_System.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for booking a Fixed Deposit (FD).
 * <p>
 * This DTO is used to receive input from the client when creating a new FD.
 * It includes validation annotations to ensure valid data is sent
 * from the frontend before persisting to the database.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFDRequest {

    /**
     * Unique identifier for the Fixed Deposit (optional for creation).
     */
    private Long id;

    /**
     * ID of the user who is booking the Fixed Deposit.
     */
    @NotNull(message = "User ID is required")
    private Long userId;

    /**
     * The amount to be deposited.
     */
    @NotNull(message = "Deposit amount is required")
    @Min(value = 1000, message = "Minimum deposit amount is 1000")
    private BigDecimal amount;

    /**
     * The name of the FD scheme chosen by the user.
     */
    @NotNull(message = "Scheme is required")
    private String scheme;

    /**
     * Duration of the Fixed Deposit in months.
     */
    @NotNull(message = "Tenure (in months) is required")
    @Min(value = 1, message = "Tenure must be at least 1 month")
    private Integer tenureMonths;

    /**
     * Interest rate applicable to the Fixed Deposit.
     */
    @NotNull(message = "Interest rate is required")
    @Min(value = 1, message = "Interest rate must be greater than 0")
    private BigDecimal interestRate;

    /**
     * Date when the Fixed Deposit starts.
     */
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    /**
     * Date when the Fixed Deposit matures.
     * This can be auto-calculated based on startDate and tenureMonths.
     */
    private LocalDate maturityDate;
}

















//
////for client===input from frontend → backend → database
//package com.zeta.Digital_Fixed_Deposit_System.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class BookFDRequest {
//    private Long id;
//    private Long userId;
//    private BigDecimal amount;
//    private String scheme;
//    private Integer tenureMonths;
//    private BigDecimal interestRate;
//    private LocalDate startDate;
//    private LocalDate maturityDate;
//}














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
