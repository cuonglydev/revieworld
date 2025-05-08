package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Entity.Deposit;
import com.example.Entity.User;
import java.util.UUID;

@Service
public class UsdtService {
    
    @Autowired
    private DepositService depositService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SystemSettingService systemSettingService;

    public String generateUsdtAddress() {
        // In real implementation, this should integrate with your USDT wallet provider
        // For now, return a dummy address
        return "TRC20-" + UUID.randomUUID().toString();
    }
    
    public String createPayment(Double amount, User user) {
        // Generate a unique USDT address for this transaction
        String usdtAddress = generateUsdtAddress();
        
        // Create deposit record
        Deposit deposit = new Deposit();
        deposit.setAmount(amount);
        deposit.setUser(user);
        deposit.setPaymentMethod("USDT");
        deposit.setTransactionId("USDT-" + UUID.randomUUID().toString());
        deposit.setStatus("PENDING");
        depositService.save(deposit);
        
        // Return the USDT address where user should send funds
        return usdtAddress;
    }
    
    @Transactional
    public boolean processUsdtCallback(String transactionId, String txHash, Double amount, String fromAddress) {
        try {
            // Verify the transaction on blockchain
            if (!verifyUsdtTransaction(txHash, fromAddress, amount)) {
                return false;
            }
            
            // Find and update deposit
            Deposit deposit = depositService.findByTransactionId(transactionId);
            if (deposit == null || !deposit.getStatus().equals("PENDING")) {
                return false;
            }
            
            // Verify amount matches
            if (!deposit.getAmount().equals(amount)) {
                deposit.setStatus("FAILED");
                depositService.save(deposit);
                return false;
            }
            
            // Update deposit status
            deposit.setStatus("SUCCESS");
            depositService.save(deposit);
            
            // Update user balance
            User user = deposit.getUser();
            user.setAmount(user.getAmount() + amount);
            userService.save(user);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean verifyUsdtTransaction(String txHash, String fromAddress, Double amount) {
        // In real implementation, this should verify the transaction on the blockchain
        // For now, return true for testing
        return true;
    }
} 