package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Entity.SystemSetting;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Integer> {
    
    @Query("SELECT s FROM SystemSetting s ORDER BY s.id DESC LIMIT 1")
    SystemSetting findLatestSettings();
} 