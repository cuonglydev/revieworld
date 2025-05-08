package com.example.Controller.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Service.UsdtService;
import java.util.Map;

@RestController
public class UsdtCallbackController {

    @Autowired
    private UsdtService usdtService;

    @PostMapping("/usdt/callback")
    public ResponseEntity<String> usdtCallback(@RequestBody Map<String, Object> data) {
        try {
            // Extract data from callback
            String transactionId = (String) data.get("transaction_id");
            String txHash = (String) data.get("tx_hash");
            Double amount = Double.valueOf(data.get("amount").toString());
            String fromAddress = (String) data.get("from_address");
            
            // Validate required fields
            if (transactionId == null || txHash == null || amount == null || fromAddress == null) {
                return ResponseEntity.badRequest().body("Missing required fields");
            }
            
            // Process the USDT payment
            boolean success = usdtService.processUsdtCallback(transactionId, txHash, amount, fromAddress);
            
            if (success) {
                return ResponseEntity.ok("OK");
            } else {
                return ResponseEntity.badRequest().body("Failed to process payment");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }
} 