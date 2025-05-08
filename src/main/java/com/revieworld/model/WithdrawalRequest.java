package com.revieworld.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "withdrawal_requests")
public class WithdrawalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String bankAccount;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status;

    private String rejectionReason;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    public enum WithdrawalStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
} 