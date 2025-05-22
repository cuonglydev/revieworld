package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Entity.WithdrawSetting;

@Repository
public interface WithdrawSettingRepository extends JpaRepository<WithdrawSetting, Integer> {
    // Single record with ID = 1
    default WithdrawSetting getDefault() {
        return findById(1).orElse(new WithdrawSetting());
    }
} 