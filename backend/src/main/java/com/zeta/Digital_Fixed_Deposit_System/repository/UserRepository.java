package com.zeta.Digital_Fixed_Deposit_System.repository;

import com.zeta.Digital_Fixed_Deposit_System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
