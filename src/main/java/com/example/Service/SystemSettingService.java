package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.SystemSetting;
import com.example.Repository.SystemSettingRepository;

@Service
public class SystemSettingService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    public SystemSetting getLatestSettings() {
        return systemSettingRepository.findLatestSettings();
    }

    public SystemSetting saveSettings(SystemSetting settings) {
        return systemSettingRepository.save(settings);
    }
} 