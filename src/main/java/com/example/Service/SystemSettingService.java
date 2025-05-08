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
        return systemSettingRepository.findFirstByOrderByIdDesc();
    }
    
    public SystemSetting saveSettings(SystemSetting settings) {
        return systemSettingRepository.save(settings);
    }
    
    public Double getMinDeposit() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getMinDepositAmount() : 5.0; // Default minimum deposit
    }
    
    public Double getMaxDeposit() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getMaxDepositAmount() : 10000.0; // Default maximum deposit
    }
    
    public Double getUsdToVndRate() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getUsdToVndRate() : 23000.0; // Default exchange rate
    }
    
    public Double getCurrencyExchangeRate() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getCurrencyExchangeRate() : getUsdToVndRate(); // Fallback to USD/VND rate
    }
    
    public Double getCommissionRate() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getCommissionRate() : 5.0; // Default commission rate
    }
    
    public Double getAffiliateCommissionPercentage() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getAffiliateCommissionPercentage() : 5.0; // Default affiliate commission
    }
    
    public Double getReferralCommissionRate() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getReferralCommissionRate() : 5.0; // Default referral commission
    }
    
    public String getSupportTelegramLink() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getSupportTelegramLink() : ""; // Default empty link
    }
} 