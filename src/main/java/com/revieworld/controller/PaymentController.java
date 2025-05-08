package com.revieworld.controller;

import com.revieworld.entity.Payment;
import com.revieworld.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, String> request) {
        String orderId = request.get("orderId");
        BigDecimal amount = new BigDecimal(request.get("amount"));
        String currency = request.get("currency");

        Payment payment = paymentService.createPayment(orderId, amount, currency);
        String qrCode = paymentService.generateQRCode(payment);

        return ResponseEntity.ok(Map.of(
            "paymentId", payment.getId(),
            "orderId", payment.getOrderId(),
            "amount", payment.getAmount(),
            "currency", payment.getCurrency(),
            "status", payment.getStatus(),
            "bankAccount", payment.getBankAccount(),
            "bankName", payment.getBankName(),
            "qrCode", qrCode
        ));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String orderId) {
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handlePaymentWebhook(@RequestBody Map<String, String> webhook) {
        String orderId = webhook.get("orderId");
        String status = webhook.get("status");
        String transactionId = webhook.get("transactionId");

        Payment payment = paymentService.updatePaymentStatus(orderId, status, transactionId);
        return ResponseEntity.ok(payment);
    }
} 