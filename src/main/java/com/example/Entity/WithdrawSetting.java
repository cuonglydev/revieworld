package com.example.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "withdraw_settings")
public class WithdrawSetting {
    
    @Id
    @Column(name = "id")
    private Integer id = 1; // Always 1 for single record
    
    @Column(name = "min_withdraw_amount")
    private Double minWithdrawAmount = 10.0;
    
    @Column(name = "max_withdraw_amount")
    private Double maxWithdrawAmount = 10000.0;
    
    @Column(name = "daily_withdraw_limit")
    private Double dailyWithdrawLimit = 50000.0;
    
    @Column(name = "withdraw_fee_percentage")
    private Double withdrawFeePercentage = 1.0;
    
    @Column(name = "min_balance_required")
    private Double minBalanceRequired = 0.0;
    
    @Column(name = "processing_time_hours")
    private Integer processingTimeHours = 24;
    
    @Column(name = "status")
    private Boolean status = true;
} 