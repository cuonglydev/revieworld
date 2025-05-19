package com.example.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Entity.Affiliate;
import com.example.Entity.Deposit;
import com.example.Entity.User;
import com.example.Entity.UserAffiliate;
import com.example.Entity.UserAffiliateItem;
import com.example.Service.AffiliateService;
import com.example.Service.DepositService;
import com.example.Service.UserAffiliateItemService;
import com.example.Service.UserAffiliateService;
import com.example.Service.UserService;

import java.util.Date;
import java.util.Map;

@RestController
public class UsdtCallbackController {

	/*
	 * @Autowired private UsdtService usdtService;
	 */
	
	@Autowired
	private AffiliateService affiliateService;
    
	@Autowired
	private UserAffiliateItemService userAffiliateItemService;
	
	@Autowired
	private UserAffiliateService userAffiliateService;
    
    @Autowired
    private DepositService depositService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/api/fpayment/callback")
    public ResponseEntity<Map<String, Object>> handleFpaymentCallback(
            @RequestParam String request_id,
            @RequestParam String merchant_id,
            @RequestParam String api_key,
            @RequestParam String received,
            @RequestParam String status,
            @RequestParam(required = false) String from_address,
            @RequestParam(required = false) String transaction_id
    ) {
        // Log đầu vào
        System.out.println("Callback received with: request_id=" + request_id + ", merchant_id=" + merchant_id + ", api_key=" + api_key);

        final String expectedMerchantId = "67b01a62713ef";
        final String expectedApiKey = "438e03b2973f1ec5c58e01d5e6880da367b01a62713f7";

        // Xác thực callback
        if (!merchant_id.equals(expectedMerchantId) || !api_key.equals(expectedApiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("status", "error", "message", "Merchant ID hoặc API Key không hợp lệ."));
        }

        // Tìm giao dịch theo request_id
        Deposit deposit = depositService.findByTransactionId(request_id);
        if (deposit == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Không tìm thấy giao dịch."));
        }

        
        // Xử lý theo trạng thái
        switch (status.toLowerCase()) {
            case "waiting":
                deposit.setStatus("WAITING");
                break;
            case "expired":
                deposit.setStatus("EXPIRED");
                break;
            case "completed":
                deposit.setStatus("SUCCESS");
                deposit.setNote("From: " + from_address + ", Tx: " + transaction_id);
                try {
                    double receivedAmount = Double.parseDouble(received);
                    User user = userService.findById(deposit.getUser().getId());
                    user.setAmount(user.getAmount() + receivedAmount);
                    userService.save(user);
                    deposit.setAmount(receivedAmount);
                    
                    Date currentDate = new Date();
                  
                		if(user.getUserAffiliate() != null) {
                			UserAffiliate userAffiliate = userAffiliateService.findById(user.getUserAffiliate().getId());
                			if(userAffiliate != null) {
                    			User inviter = userService.findById(userAffiliate.getUser().getId());
                    			
                    			Double fee = deposit.getAmount();
                    			UserAffiliateItem userAffiliateItem = new UserAffiliateItem();
                    			userAffiliateItem.setAmount(fee);
                    			userAffiliateItem.setCreatedAt(currentDate);
                    			userAffiliateItem.setUser(inviter);
                    			userAffiliateItem.setUserAffiliate(userAffiliate);
                    			userAffiliateItemService.save(userAffiliateItem);
                    			
                    			userAffiliate.setAmount(userAffiliate.getAmount() + fee);
                    			userAffiliateService.save(userAffiliate);
                    		}
                		}
                	
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Số tiền không hợp lệ."));
                }
                break;
            default:
                return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Trạng thái giao dịch không hợp lệ."));
        }

        Date currentDate = new Date();
        deposit.setCreatedAt(currentDate);
        depositService.save(deposit);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Callback đã được xử lý thành công."));
    }
    

	/*
	 * @PostMapping("/usdt/callback") public ResponseEntity<String>
	 * usdtCallback(@RequestBody Map<String, Object> data) { try { // Extract data
	 * from callback String transactionId = (String) data.get("transaction_id");
	 * String txHash = (String) data.get("tx_hash"); Double amount =
	 * Double.valueOf(data.get("amount").toString()); String fromAddress = (String)
	 * data.get("from_address");
	 * 
	 * // Validate required fields if (transactionId == null || txHash == null ||
	 * amount == null || fromAddress == null) { return
	 * ResponseEntity.badRequest().body("Missing required fields"); }
	 * 
	 * // Process the USDT payment boolean success =
	 * usdtService.processUsdtCallback(transactionId, txHash, amount, fromAddress);
	 * 
	 * if (success) { return ResponseEntity.ok("OK"); } else { return
	 * ResponseEntity.badRequest().body("Failed to process payment"); } } catch
	 * (Exception e) { e.printStackTrace(); return
	 * ResponseEntity.internalServerError().body("Internal server error"); } }
	 */
} 