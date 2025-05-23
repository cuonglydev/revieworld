package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Entity.SupportSetting;

@Repository
public interface SupportSettingRepository extends JpaRepository<SupportSetting, Integer> {
    // Single record with ID = 1
    default SupportSetting getDefault() {
        return findById(1).orElse(new SupportSetting());
    }
} 