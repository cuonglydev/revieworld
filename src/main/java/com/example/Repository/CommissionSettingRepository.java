package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Entity.CommissionSetting;

@Repository
public interface CommissionSettingRepository extends JpaRepository<CommissionSetting, Integer> {
    // Single record with ID = 1
    default CommissionSetting getDefault() {
        return findById(1).orElse(new CommissionSetting());
    }
} 