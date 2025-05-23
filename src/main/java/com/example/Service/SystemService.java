package com.example.Service;

import com.example.Entity.SystemSetting;

public interface SystemService {
    // Deposit related
    Double getMinDepositAmount();
    Double getMaxDepositAmount();
    void updateDepositLimits(Double minAmount, Double maxAmount);
    
    // Commission related
    Double getCommissionRate();
    Double getNewUserCommissionRate();
    Double getAffiliateCommissionRate();
    Double getReferralCommissionRate();
    void updateCommissionRates(Double commissionRate, Double newUserRate, Double affiliateRate, Double referralRate);
    
    // Currency related
    Double getUsdToVndRate();
    Double getCurrencyExchangeRate();
    void updateCurrencyRates(Double usdToVndRate, Double exchangeRate);
    
    // Support related
    String getSupportTelegramLink();
    String getSupportEmail();
    void updateSupportInfo(String telegramLink, String email);
    
    // General settings
    SystemSetting getLatestSettings();
    SystemSetting saveSettings(SystemSetting settings);
    void validateSettings(SystemSetting settings);

    Double getMinWithdrawAmount();
    Double getMaxWithdrawAmount();
} 