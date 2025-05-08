package com.revieworld.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_settings")
@Data
public class SystemSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Deposit settings
    private BigDecimal minDepositAmount;
    private BigDecimal maxDepositAmount;

    // Commission settings
    private Double newUserCommissionPercentage;

    // Support information
    private String supportEmail;
    private String supportPhone;
    private String supportTelegram;
    private String supportZalo;

    // Exchange rate
    private BigDecimal usdToVndRate;
    private LocalDateTime exchangeRateLastUpdated;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 