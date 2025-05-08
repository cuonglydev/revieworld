package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Entity.SystemSetting;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Integer> {
    SystemSetting findFirstByOrderByIdDesc();
} 