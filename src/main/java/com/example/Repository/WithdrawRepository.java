package com.example.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Withdraw;

@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw, Integer> {
    List<Withdraw> findByUserId(int userId);
} 