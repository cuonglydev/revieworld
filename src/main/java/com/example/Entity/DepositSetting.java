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
@Table(name = "deposit_settings")
public class DepositSetting {
    
    @Id
    @Column(name = "id")
    private Integer id = 1; // Always 1 for single record
    
    @Column(name = "min_deposit_amount")
    private Double minDepositAmount = 5.0;
    
    @Column(name = "max_deposit_amount")
    private Double maxDepositAmount = 10000.0;
    
    @Column(name = "usd_to_vnd_rate")
    private Double usdToVndRate = 23000.0;
    
    @Column(name = "currency_exchange_rate")
    private Double currencyExchangeRate = 23000.0;
    
    @Column(name = "status")
    private Boolean status = true;
} 