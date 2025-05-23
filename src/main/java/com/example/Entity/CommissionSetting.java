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
@Table(name = "commission_settings")
public class CommissionSetting {
    
    @Id
    @Column(name = "id")
    private Integer id = 1; // Always 1 for single record
    
    @Column(name = "base_commission_percentage")
    private Double baseCommissionPercentage = 5.0;
    
    @Column(name = "new_user_commission_percentage")
    private Double newUserCommissionPercentage = 5.0;
    
    @Column(name = "referral_commission_percentage")
    private Double referralCommissionPercentage = 2.0;
    
    @Column(name = "min_commission_amount")
    private Double minCommissionAmount = 10.0;
    
    @Column(name = "max_commission_amount")
    private Double maxCommissionAmount = 1000.0;
    
    @Column(name = "commission_waiting_days")
    private Integer commissionWaitingDays = 0;
    
    @Column(name = "status")
    private Boolean status = true;
} 