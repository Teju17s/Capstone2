package com.zeta.Digital_Fixed_Deposit_System.controller;

import com.zeta.Digital_Fixed_Deposit_System.dto.ApiResponse;
import com.zeta.Digital_Fixed_Deposit_System.dto.BookFDRequest;
import com.zeta.Digital_Fixed_Deposit_System.dto.FixedDepositResponse;
import com.zeta.Digital_Fixed_Deposit_System.entity.FixedDeposit;
import com.zeta.Digital_Fixed_Deposit_System.service.FixedDepositService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fd")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class FixedDepositController {

    private final FixedDepositService fixedDepositService;

    public FixedDepositController(FixedDepositService fixedDepositService) {
        this.fixedDepositService = fixedDepositService;
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<FixedDepositResponse>> bookFixedDeposit(@RequestBody BookFDRequest request) {

        if (request.getAmount() == null || request.getAmount().doubleValue() < 1000) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid deposit amount. Minimum amount should be 1000."));
        }

        FixedDeposit fixedDeposit = fixedDepositService.bookFixedDeposit(request);

        FixedDepositResponse response = mapToResponse(fixedDeposit);

        return ResponseEntity.ok(ApiResponse.success("Fixed deposit booked successfully.", response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<FixedDepositResponse>>> getFixedDepositsForUser(@PathVariable Long userId) {
        List<FixedDeposit> fixedDeposits = fixedDepositService.getFixedDepositsByUser(userId);

        List<FixedDepositResponse> responseList = fixedDeposits.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Fixed deposits retrieved successfully.", responseList));
    }

    // ---------------------------
    // Utility method: Entity -> DTO
    // ---------------------------
    private FixedDepositResponse mapToResponse(FixedDeposit fd) {
        return FixedDepositResponse.builder()
                .fixedDepositId(fd.getFixedDepositId())
                .userId(fd.getUser().getUserId())  // assuming User entity has getId()
                .amount(fd.getAmount())
                .tenureMonths(fd.getTenureMonths())
                .interestRate(fd.getInterestRate())
                .startDate(fd.getStartDate())
                .maturityDate(fd.getMaturityDate())
                .status(fd.getStatus())
                .scheme(fd.getScheme())
                .accruedInterest(fd.getAccruedInterest())
                .createdAt(fd.getCreatedAt())
                .build();
    }
}




















//package com.zeta.Digital_Fixed_Deposit_System.controller;
//
//import com.zeta.Digital_Fixed_Deposit_System.dto.ApiResponse;
//import com.zeta.Digital_Fixed_Deposit_System.dto.BookFDRequest;
//import com.zeta.Digital_Fixed_Deposit_System.entity.FixedDeposit;
//import com.zeta.Digital_Fixed_Deposit_System.service.FixedDepositService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/fd")
//@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
//public class FixedDepositController {
//
//    private final FixedDepositService fixedDepositService;
//
//    public FixedDepositController(FixedDepositService fixedDepositService) {
//        this.fixedDepositService = fixedDepositService;
//    }
//
//    @PostMapping("/book")
//    public ResponseEntity<ApiResponse<FixedDeposit>> bookFixedDeposit(@RequestBody BookFDRequest request) {
//        if (request.getAmount() == null || request.getAmount().doubleValue() < 1000) {
//            return ResponseEntity.badRequest()
//                    .body(ApiResponse.error("Invalid deposit amount. Minimum amount should be 1000."));
//        }
//
//        FixedDeposit fixedDeposit = fixedDepositService.bookFixedDeposit(request);
//
//
//
//        return ResponseEntity.ok(ApiResponse.success("Fixed deposit booked successfully.", fixedDeposit));
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<ApiResponse<List<FixedDeposit>>> getFixedDepositsForUser(@PathVariable Long userId) {
//        List<FixedDeposit> fixedDeposits = fixedDepositService.getFixedDepositsByUser(userId);
//
//        return ResponseEntity.ok(ApiResponse.success("Fixed deposits retrieved successfully.", fixedDeposits));
//    }
//}
