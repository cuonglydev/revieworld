package com.example.Controller.User;

import com.example.Entity.Deposit;
import com.example.Entity.User;
import com.example.Repository.DepositRepository;
import com.example.Service.UserService;
import com.example.Service.SystemSettingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.codec.binary.Hex;

@RestController
public class SepayCallbackController {
    @Autowired
    private DepositRepository depositRepository;
    
    @Autowired
    private UserService userService;
    
    @Value("${sepay.secret.key}")
    private String sepaySecretKey;

    @PostMapping("/sepay/callback")
    public ResponseEntity<String> sepayCallback(
            @RequestBody Map<String, Object> data,
            @RequestHeader("X-Sepay-Signature") String signature) {
        try {
            // Verify signature
            if (!verifySignature(data, signature)) {
                return ResponseEntity.badRequest().body("Invalid signature");
            }

            String orderId = (String) data.get("order_id");
            String status = (String) data.get("status");
            Double amount = Double.valueOf(data.get("amount").toString());

            // Find deposit by transaction ID
            Deposit deposit = depositRepository.findByTransactionId(orderId);
            if (deposit == null) {
                return ResponseEntity.badRequest().body("Order not found");
            }

            // Verify amount matches
            if (!deposit.getAmount().equals(amount)) {
                deposit.setStatus("FAILED");
                depositRepository.save(deposit);
                return ResponseEntity.badRequest().body("Amount mismatch");
            }

            // Only process pending deposits
            if (!deposit.getStatus().equals("PENDING")) {
                return ResponseEntity.ok("Order already processed");
            }

            if (status.equalsIgnoreCase("SUCCESS")) {
                deposit.setStatus("SUCCESS");
                depositRepository.save(deposit);

                // Update user balance
                User user = deposit.getUser();
                user.setAmount(user.getAmount() + amount);
                userService.save(user);

                return ResponseEntity.ok("OK");
            } else {
                deposit.setStatus("FAILED");
                depositRepository.save(deposit);
                return ResponseEntity.ok("Payment failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    private boolean verifySignature(Map<String, Object> data, String receivedSignature) {
        try {
            // Sort parameters alphabetically
            TreeMap<String, Object> sortedParams = new TreeMap<>(data);
            
            // Create string to sign
            StringBuilder stringToSign = new StringBuilder();
            for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
                if (entry.getValue() != null) {
                    stringToSign.append(entry.getKey())
                               .append("=")
                               .append(entry.getValue())
                               .append("&");
                }
            }
            
            // Remove last &
            if (stringToSign.length() > 0) {
                stringToSign.setLength(stringToSign.length() - 1);
            }
            
            // Calculate HMAC
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(sepaySecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(secretKey);
            byte[] hash = hmac.doFinal(stringToSign.toString().getBytes(StandardCharsets.UTF_8));
            String calculatedSignature = Hex.encodeHexString(hash);
            
            return calculatedSignature.equals(receivedSignature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 