package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Entity.DepositSetting;

@Repository
public interface DepositSettingRepository extends JpaRepository<DepositSetting, Integer> {
    // Single record with ID = 1
    default DepositSetting getDefault() {
        return findById(1).orElse(new DepositSetting());
    }
} 