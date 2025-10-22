package com.zeta.Digital_Fixed_Deposit_System.repository;

import com.zeta.Digital_Fixed_Deposit_System.entity.FixedDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedDepositRepository extends JpaRepository<FixedDeposit, Long> {
    List<FixedDeposit> findByUserUserId(Long userId);
}
