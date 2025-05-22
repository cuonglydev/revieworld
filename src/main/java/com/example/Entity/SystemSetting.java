package com.example.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "currency_exchange_rate")
    private Double currencyExchangeRate;

    @Column(name = "min_withdraw_amount")
    private Double minWithdrawAmount;

    @Column(name = "max_withdraw_amount")
    private Double maxWithdrawAmount;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void updateAuditFields(String username) {
        if (this.id == 0) {
            this.createdBy = username;
            this.createdAt = LocalDateTime.now();
        }
        this.updatedBy = username;
        this.updatedAt = LocalDateTime.now();
    }
} 