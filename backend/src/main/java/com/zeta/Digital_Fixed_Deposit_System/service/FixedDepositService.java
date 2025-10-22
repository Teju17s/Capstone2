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
import java.util.stream.Collectors;

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
     * Retrieves all Fixed Deposits for a specific user.
     */
    public List<FixedDeposit> getFixedDepositsByUser(Long userId) {
        //return fixedDepositRepository.findByUserUserId(userId);
        List<FixedDeposit> fds = fixedDepositRepository.findByUserUserId(userId);

        // Recalculate accrued interest dynamically
        fds.forEach(fd -> fd.setAccruedInterest(calculateAccruedInterest(fd)));

        return fds;
    }

    /**
     * Calculates accrued interest for a given Fixed Deposit (simple daily interest).
     */
    public BigDecimal calculateAccruedInterest(FixedDeposit fd) {
        LocalDate startDate = fd.getStartDate() != null ? fd.getStartDate() : LocalDate.now();
        LocalDate today = LocalDate.now();
        long daysElapsed = ChronoUnit.DAYS.between(startDate, today);

        BigDecimal principal = fd.getAmount();
        BigDecimal annualRate = fd.getInterestRate().divide(BigDecimal.valueOf(100), MATH_CONTEXT);

        BigDecimal accrued = principal
                .multiply(annualRate)
                .multiply(BigDecimal.valueOf(daysElapsed))
                .divide(BigDecimal.valueOf(365), DISPLAY_SCALE, RoundingMode.HALF_UP);

        return accrued;
    }

    /**
     * Returns the interest rate for a given FD scheme.
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
