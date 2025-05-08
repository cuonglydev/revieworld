package com.revieworld.repository;

import com.revieworld.model.SystemSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemSettingsRepository extends JpaRepository<SystemSettings, Long> {
    SystemSettings findFirstByOrderByIdDesc();
} 