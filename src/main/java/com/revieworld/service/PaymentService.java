package com.revieworld.service;

import com.revieworld.entity.Payment;
import java.math.BigDecimal;

public interface PaymentService {
    Payment createPayment(String orderId, BigDecimal amount, String currency);
    Payment getPaymentByOrderId(String orderId);
    Payment updatePaymentStatus(String orderId, String status, String transactionId);
    String generateQRCode(Payment payment);
} 