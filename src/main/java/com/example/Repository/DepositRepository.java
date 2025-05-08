package com.example.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Deposit;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Integer> {
    List<Deposit> findByUserId(int userId);
    Deposit findByTransactionId(String transactionId);
} 