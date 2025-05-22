package com.example.Service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Entity.SystemSetting;
import com.example.Repository.SystemSettingRepository;
import com.example.Service.SystemService;

@Service
public class SystemServiceImpl implements SystemService {

    private static final Logger logger = LoggerFactory.getLogger(SystemServiceImpl.class);

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    @Override
    public Double getMinDepositAmount() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getMinDepositAmount() : 5.0;
    }

    @Override
    public Double getMaxDepositAmount() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getMaxDepositAmount() : 10000.0;
    }

    @Override
    @Transactional
    public void updateDepositLimits(Double minAmount, Double maxAmount) {
        logger.info("Updating deposit limits - Min: {}, Max: {}", minAmount, maxAmount);
        SystemSetting settings = getLatestSettings();
        if (settings == null) {
            settings = new SystemSetting();
            logger.info("Creating new system settings");
        }
        settings.setMinDepositAmount(minAmount);
        settings.setMaxDepositAmount(maxAmount);
        systemSettingRepository.save(settings);
        logger.info("Successfully updated deposit limits");
    }

    @Override
    public Double getCommissionRate() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getCommissionRate() : 5.0;
    }

    @Override
    public Double getNewUserCommissionRate() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getNewUserCommissionRate() : 5.0;
    }

    @Override
    public Double getAffiliateCommissionRate() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getAffiliateCommissionPercentage() : 5.0;
    }

    @Override
    public Double getReferralCommissionRate() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getReferralCommissionRate() : 5.0;
    }

    @Override
    @Transactional
    public void updateCommissionRates(Double commissionRate, Double newUserRate, Double affiliateRate, Double referralRate) {
        logger.info("Updating commission rates - Commission: {}%, NewUser: {}%, Affiliate: {}%, Referral: {}%", 
                   commissionRate, newUserRate, affiliateRate, referralRate);
        SystemSetting settings = getLatestSettings();
        if (settings == null) {
            settings = new SystemSetting();
            logger.info("Creating new system settings");
        }
        settings.setCommissionRate(commissionRate);
        settings.setNewUserCommissionRate(newUserRate);
        settings.setAffiliateCommissionPercentage(affiliateRate);
        settings.setReferralCommissionRate(referralRate);
        systemSettingRepository.save(settings);
        logger.info("Successfully updated commission rates");
    }

    @Override
    public Double getUsdToVndRate() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getUsdToVndRate() : 23000.0;
    }

    @Override
    public Double getCurrencyExchangeRate() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getCurrencyExchangeRate() : 23000.0;
    }

    @Override
    @Transactional
    public void updateCurrencyRates(Double usdToVndRate, Double exchangeRate) {
        logger.info("Updating currency rates - USD/VND: {}, Exchange Rate: {}", usdToVndRate, exchangeRate);
        SystemSetting settings = getLatestSettings();
        if (settings == null) {
            settings = new SystemSetting();
            logger.info("Creating new system settings");
        }
        settings.setUsdToVndRate(usdToVndRate);
        settings.setCurrencyExchangeRate(exchangeRate);
        systemSettingRepository.save(settings);
        logger.info("Successfully updated currency rates");
    }

    @Override
    public String getSupportTelegramLink() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getSupportTelegramLink() : "";
    }

    @Override
    public String getSupportEmail() {
        SystemSetting settings = getLatestSettings();
        return settings != null ? settings.getSupportEmail() : "";
    }

    @Override
    @Transactional
    public void updateSupportInfo(String telegramLink, String email) {
        logger.info("Updating support information - Telegram: {}, Email: {}", telegramLink, email);
        SystemSetting settings = getLatestSettings();
        if (settings == null) {
            settings = new SystemSetting();
            logger.info("Creating new system settings");
        }
        settings.setSupportTelegramLink(telegramLink);
        settings.setSupportEmail(email);
        systemSettingRepository.save(settings);
        logger.info("Successfully updated support information");
    }

    @Override
    public SystemSetting getLatestSettings() {
        return systemSettingRepository.findLatestSettings();
    }

    @Override
    @Transactional
    public SystemSetting saveSettings(SystemSetting settings) {
        logger.info("Saving system settings");
        try {
        validateSettings(settings);
            SystemSetting savedSettings = systemSettingRepository.save(settings);
            logger.info("Successfully saved system settings");
            return savedSettings;
        } catch (Exception e) {
            logger.error("Error saving system settings: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void validateSettings(SystemSetting settings) {
        if (settings == null) {
            throw new IllegalArgumentException("Cài đặt không thể để trống");
        }

        // Validate deposit amounts
        if (settings.getMinDepositAmount() == null || settings.getMaxDepositAmount() == null) {
            throw new IllegalArgumentException("Số tiền nạp tối thiểu và tối đa không thể để trống");
        }
        if (settings.getMinDepositAmount() < 0 || settings.getMaxDepositAmount() < 0) {
            throw new IllegalArgumentException("Số tiền nạp không thể âm");
        }
        if (settings.getMinDepositAmount() > settings.getMaxDepositAmount()) {
            throw new IllegalArgumentException("Số tiền nạp tối thiểu không thể lớn hơn số tiền nạp tối đa");
        }

        // Validate exchange rates
        if (settings.getUsdToVndRate() == null) {
            throw new IllegalArgumentException("Tỉ giá USD/VND không thể để trống");
        }
        if (settings.getUsdToVndRate() <= 0) {
            throw new IllegalArgumentException("Tỉ giá USD/VND phải lớn hơn 0");
        }

        // Validate commission rates
        if (settings.getCommissionRate() == null || settings.getNewUserCommissionRate() == null || 
            settings.getAffiliateCommissionPercentage() == null || settings.getReferralCommissionRate() == null) {
            throw new IllegalArgumentException("Các tỉ lệ hoa hồng không thể để trống");
        }
        if (settings.getCommissionRate() < 0 || settings.getCommissionRate() > 100) {
            throw new IllegalArgumentException("Tỉ lệ hoa hồng phải từ 0-100%");
        }
        if (settings.getNewUserCommissionRate() < 0 || settings.getNewUserCommissionRate() > 100) {
            throw new IllegalArgumentException("Tỉ lệ hoa hồng người dùng mới phải từ 0-100%");
        }
        if (settings.getAffiliateCommissionPercentage() < 0 || settings.getAffiliateCommissionPercentage() > 100) {
            throw new IllegalArgumentException("Tỉ lệ hoa hồng giới thiệu phải từ 0-100%");
        }
        if (settings.getReferralCommissionRate() < 0 || settings.getReferralCommissionRate() > 100) {
            throw new IllegalArgumentException("Tỉ lệ hoa hồng người được giới thiệu phải từ 0-100%");
        }

        // Validate support information
        if (settings.getSupportEmail() != null && !settings.getSupportEmail().isEmpty()) {
            // Không kiểm tra định dạng, chỉ cần không rỗng là được
        }
    }
} 