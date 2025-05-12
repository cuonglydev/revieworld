package com.example.RestController;

import com.example.Entity.Deposit;
import com.example.Entity.Rate;
import com.example.Entity.User;
import com.example.Repository.DepositRepository;
import com.example.Service.UserService;
import com.example.Service.AffiliateService;
import com.example.Service.DepositService;
import com.example.Service.RateService;
import com.example.Service.UserAffiliateItemService;
import com.example.Service.UserAffiliateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.codec.binary.Hex;

@RestController
public class SepayCallbackController {
    @Autowired
    private DepositRepository depositRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    DepositService depositService;
    
    @Autowired
    RateService rateService;
    
    @Autowired
    private UserAffiliateService userAffiliateService;
    
    @Autowired
    private UserAffiliateItemService userAffiliateItemService;
    
    @Autowired
    private AffiliateService affiliateService;
	/*
	 * @Value("${sepay.secret.key}") private String sepaySecretKey;
	 */
    @PostMapping("/sepay/callback")
    public ResponseEntity<Map<String, Object>> handleSepayCallback(@RequestBody Map<String, Object> payload) {
        System.out.println("Received payload: " + payload);
        
        Rate rate = rateService.findByCountry("vi");
      
        if (!payload.containsKey("id")) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("success", false, "error", "Missing transaction id"));
        }
        
        Double amount = null;
        Object amountObj = payload.get("transferAmount");
        System.out.println("amount: " + amountObj);
        if (amountObj instanceof Number) {
            amount = ((Number) amountObj).doubleValue();
        } else if (amountObj instanceof String) {
            try {
                amount = Double.parseDouble((String) amountObj);
            } catch (NumberFormatException e) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("success", false, "error", "Invalid amount format"));
            }
        }

        if (amount == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("success", false, "error", "Amount is missing"));
        }
        
        System.out.println("-> " + amount);

        
        String code = (String) payload.get("code");
        
        Deposit deposit = depositService.findByTransactionId(code);
        if(deposit != null) {
        	
        	deposit.setAmount(amount / rate.getRate());
        	
        	deposit.setStatus("SUCCESS");
        	
        	Date currentDate = new Date();
            deposit.setCreatedAt(currentDate);
        	depositService.save(deposit);
        	
        	User user = userService.findById(deposit.getUser().getId());
        	
        	Double userAmount = user.getAmount() + amount / rate.getRate();
        	user.setAmount(userAmount);
        	userService.save(user);
        }
        

        
        return ResponseEntity
                .status(HttpStatus.CREATED) 
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("success", true));
    }
    
	/*
	 * private boolean verifySignature(Map<String, Object> data, String
	 * receivedSignature) { try { // Sort parameters alphabetically TreeMap<String,
	 * Object> sortedParams = new TreeMap<>(data);
	 * 
	 * // Create string to sign StringBuilder stringToSign = new StringBuilder();
	 * for (Map.Entry<String, Object> entry : sortedParams.entrySet()) { if
	 * (entry.getValue() != null) { stringToSign.append(entry.getKey()) .append("=")
	 * .append(entry.getValue()) .append("&"); } }
	 * 
	 * // Remove last & if (stringToSign.length() > 0) {
	 * stringToSign.setLength(stringToSign.length() - 1); }
	 * 
	 * // Calculate HMAC Mac hmac = Mac.getInstance("HmacSHA256"); SecretKeySpec
	 * secretKey = new
	 * SecretKeySpec(sepaySecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	 * hmac.init(secretKey); byte[] hash =
	 * hmac.doFinal(stringToSign.toString().getBytes(StandardCharsets.UTF_8));
	 * String calculatedSignature = Hex.encodeHexString(hash);
	 * 
	 * return calculatedSignature.equals(receivedSignature); } catch (Exception e) {
	 * e.printStackTrace(); return false; } }
	 */
} 