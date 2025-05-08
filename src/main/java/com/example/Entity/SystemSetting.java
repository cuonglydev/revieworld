package com.example.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "system_settings")
public class SystemSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "min_deposit_amount")
    private Double minDepositAmount;
    
    @Column(name = "max_deposit_amount")
    private Double maxDepositAmount;
    
    @Column(name = "usd_to_vnd_rate")
    private Double usdToVndRate;
    
    @Column(name = "currency_exchange_rate")
    private Double currencyExchangeRate;
    
    @Column(name = "commission_rate")
    private Double commissionRate;
    
    @Column(name = "new_user_commission_rate")
    private Double newUserCommissionRate;
    
    @Column(name = "affiliate_commission_percentage")
    private Double affiliateCommissionPercentage;
    
    @Column(name = "referral_commission_rate")
    private Double referralCommissionRate;
    
    @Column(name = "support_telegram_link")
    private String supportTelegramLink;
    
    @Column(name = "support_email")
    private String supportEmail;
    
    @Column(name = "telegram_support_link")
    private String telegramSupportLink;
    
    @Column(name = "last_updated")
    private Date lastUpdated;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdated = new Date();
    }
} 