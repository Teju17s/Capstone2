package com.zeta.Digital_Fixed_Deposit_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a user's Fixed Deposit (FD).
 * Tracks deposit details, interest, and lifecycle states (Active, Broken, Matured).
 */
@Entity
@Table(name = "fixed_deposit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixedDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fixed_deposit_id")
    private Long fixedDepositId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private Integer tenureMonths;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate maturityDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FDStatus status; // ACTIVE, BROKEN, MATURED

    @Column(name = "scheme", nullable = false)
    private String scheme;

    @Column(name = "accrued_interest", precision = 15, scale = 2)
    private BigDecimal accruedInterest;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ✅ New field for when FD is broken prematurely
    @Column(name = "broken_date")
    private LocalDate brokenDate;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (accruedInterest == null) {
            accruedInterest = BigDecimal.ZERO;
        }
        if (status == null) {
            status = FDStatus.ACTIVE;
        }
    }
}
