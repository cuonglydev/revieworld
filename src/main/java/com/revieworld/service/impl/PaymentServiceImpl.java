package com.revieworld.service.impl;

import com.revieworld.entity.Payment;
import com.revieworld.repository.PaymentRepository;
import com.revieworld.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Value("${payment.bank.account}")
    private String bankAccount;

    @Value("${payment.bank.name}")
    private String bankName;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment createPayment(String orderId, BigDecimal amount, String currency) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus("PENDING");
        payment.setBankAccount(bankAccount);
        payment.setBankName(bankName);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public Payment updatePaymentStatus(String orderId, String status, String transactionId) {
        Payment payment = getPaymentByOrderId(orderId);
        payment.setStatus(status);
        payment.setTransactionId(transactionId);
        if ("COMPLETED".equals(status)) {
            payment.setCompletedAt(LocalDateTime.now());
        }
        return paymentRepository.save(payment);
    }

    @Override
    public String generateQRCode(Payment payment) {
        // Format: BankName|AccountNumber|Amount|OrderId
        return String.format("%s|%s|%s|%s",
                payment.getBankName(),
                payment.getBankAccount(),
                payment.getAmount(),
                payment.getOrderId());
    }
} 