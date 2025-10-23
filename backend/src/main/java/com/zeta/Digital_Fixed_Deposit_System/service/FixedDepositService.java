package com.zeta.Digital_Fixed_Deposit_System.service;

import com.zeta.Digital_Fixed_Deposit_System.dto.BookFDRequest;
import com.zeta.Digital_Fixed_Deposit_System.entity.FDStatus;
import com.zeta.Digital_Fixed_Deposit_System.entity.FixedDeposit;
import com.zeta.Digital_Fixed_Deposit_System.entity.User;
import com.zeta.Digital_Fixed_Deposit_System.repository.FixedDepositRepository;
import com.zeta.Digital_Fixed_Deposit_System.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service layer for handling Fixed Deposit (FD) operations such as booking,
 * retrieving, and calculating interest for users.
 */
@Service
public class FixedDepositService {

    private final FixedDepositRepository fixedDepositRepository;
    private final UserRepository userRepository;

    private static final MathContext MATH_CONTEXT = new MathContext(12);
    private static final int DISPLAY_SCALE = 2;

    public FixedDepositService(FixedDepositRepository fixedDepositRepository,
                               UserRepository userRepository) {
        this.fixedDepositRepository = fixedDepositRepository;
        this.userRepository = userRepository;
    }

    /**
     * Books a new Fixed Deposit for a user.
     *
     * @param request details of the FD to be booked
     * @return the created FixedDeposit entity
     */
    public FixedDeposit bookFixedDeposit(BookFDRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        FixedDeposit fd = new FixedDeposit();
        fd.setUser(user);
        fd.setAmount(request.getAmount());
        fd.setInterestRate(getRateForScheme(request.getScheme()));
        fd.setScheme(request.getScheme());
        fd.setTenureMonths(request.getTenureMonths());

        LocalDate startDate = LocalDate.now();
        fd.setStartDate(startDate);
        fd.setMaturityDate(startDate.plusMonths(request.getTenureMonths()));

        fd.setStatus(FDStatus.ACTIVE);
        fd.setCreatedAt(LocalDateTime.now());
        fd.setAccruedInterest(calculateAccruedInterest(fd));

        return fixedDepositRepository.save(fd);
    }

    /**
     * Retrieves all Fixed Deposits belonging to a specific user.
     *
     * @param userId the user’s ID
     * @return list of FixedDeposits belonging to the user
     */
    public List<FixedDeposit> getFixedDepositsByUser(Long userId) {
        List<FixedDeposit> fds = fixedDepositRepository.findByUserUserId(userId);

        // Dynamically recalculate accrued interest before returning
        fds.forEach(fd -> fd.setAccruedInterest(calculateAccruedInterest(fd)));

        return fds;
    }

    /**
     * Calculates accrued interest for a given Fixed Deposit (simple daily interest).
     * Accrual stops on:
     * <ul>
     *     <li>the current date, if ACTIVE</li>
     *     <li>the broken date, if BROKEN</li>
     *     <li>the maturity date, if MATURED</li>
     * </ul>
     *
     * @param fd the Fixed Deposit entity
     * @return accrued interest as BigDecimal
     */
    public BigDecimal calculateAccruedInterest(FixedDeposit fd) {
        if (fd == null || fd.getAmount() == null || fd.getInterestRate() == null) {
            return BigDecimal.ZERO;
        }

        LocalDate startDate = fd.getStartDate() != null ? fd.getStartDate() : LocalDate.now();
        LocalDate endDate = determineAccrualEndDate(fd);

        if (endDate.isBefore(startDate)) {
            return BigDecimal.ZERO;
        }

        long daysElapsed = ChronoUnit.DAYS.between(startDate, endDate);

        BigDecimal principal = fd.getAmount();
        BigDecimal annualRate = fd.getInterestRate()
                .divide(BigDecimal.valueOf(100), MATH_CONTEXT);

        BigDecimal accrued = principal
                .multiply(annualRate)
                .multiply(BigDecimal.valueOf(daysElapsed))
                .divide(BigDecimal.valueOf(365), DISPLAY_SCALE, RoundingMode.HALF_UP);

        // Ensure no negative or invalid interest is returned
        return accrued.max(BigDecimal.ZERO);
    }

    /**
     * Determines the end date for interest accrual based on FD status.
     *
     * @param fd the Fixed Deposit entity
     * @return the appropriate end date for interest calculation
     */
    private LocalDate determineAccrualEndDate(FixedDeposit fd) {
        LocalDate today = LocalDate.now();

        return switch (fd.getStatus()) {
            case ACTIVE -> today;
            case BROKEN -> fd.getBrokenDate() != null ? fd.getBrokenDate() : today;
            case MATURED -> fd.getMaturityDate() != null ? fd.getMaturityDate() : today;
            default -> today;
        };
    }

    /**
     * Returns the applicable interest rate for a given FD scheme.
     *
     * @param scheme name of the FD scheme
     * @return interest rate as BigDecimal
     */
    private BigDecimal getRateForScheme(String scheme) {
        return switch (scheme) {
            case "Regular Saver" -> BigDecimal.valueOf(6.5);
            case "Premium Saver" -> BigDecimal.valueOf(7.0);
            case "Longterm Growth" -> BigDecimal.valueOf(7.5);
            case "Tax Saver" -> BigDecimal.valueOf(7.2);
            default -> BigDecimal.valueOf(6.5);
        };
    }
}
