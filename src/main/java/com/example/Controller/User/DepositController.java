package com.example.Controller.User;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.Common.IdGenerator;
import com.example.Entity.AdminBank;
import com.example.Entity.Deposit;
import com.example.Entity.Rate;
import com.example.Entity.User;
import com.example.Security.SecurityUtils;
import com.example.Service.AdminBankService;
import com.example.Service.DepositService;
import com.example.Service.RateService;
import com.example.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;


@Controller
public class DepositController {

	@Autowired
	private DepositService depositService;
	
	@Autowired
	private UserService userService;
	

	
	@Autowired
	private RateService rateService;
	
	@Autowired
	private AdminBankService adminBankService;
	
	@Value("${bank.bank}")
	private String bank;
	
	@Value("${bank.bank.account}")
	private String bankAccount;
	
	@Value("${sepay.qr.url}")
	private String qrUrl;
	
	@Value("${usdt.api.url}")
	private String usdtApiUrl;
	
	@Value("${usdt.merchant.id}")
	private String usdtMerchantId;
	
	@Value("${usdt.api.key}")
	private String usdtApiKey;
	
	@Value("${my.domain}")
	private String myDomain;
	
	@GetMapping("/account/deposit")
	public String depositPage(Model model) {
		User currentUser = userService.getCurrentUser();
		
		if(currentUser == null) {
			return "redirect:/login";
		}
		
		Rate rate = rateService.findByCountry("vi");
		model.addAttribute("rate", rate);
		
		if (currentUser != null) {
			model.addAttribute("currentBalance", currentUser.getAmount());
		}
		
		return "User/Pages/Account/deposit";
	}

	
	@GetMapping("/account/deposit-history")
	public String depositHistoryPage(Model model) {
		// Get current user's deposits
		User currentUser = userService.getCurrentUser();
		model.addAttribute("deposits", depositService.findByUserId(currentUser.getId()));
		return "User/Pages/Account/deposit-history";
	}
	
	@PostMapping("/account/deposit")
	public String processDeposit(@RequestParam("amount") Double amount,
							   @RequestParam("paymentMethod") String paymentMethod,
							   RedirectAttributes redirectAttributes, Model model) {
		
		User currentUser = SecurityUtils.getCurrentUser();
		
		
		if (currentUser == null) {
			redirectAttributes.addFlashAttribute("danger", "Bạn cần đăng nhập!");
			return "redirect:/login";
		}
		

		
		if (!paymentMethod.equals("SEPAY") && !paymentMethod.equals("USDT")) {
			redirectAttributes.addFlashAttribute("danger", "Phương thức thanh toán không hợp lệ");
			return "redirect:/account/deposit";
		}
		
		Rate rate = rateService.findByCountry("vi");
		

		try {
			String transactionId;
			
			Deposit findDepositByTransactionId;
			do {
				transactionId = IdGenerator.generator();
				findDepositByTransactionId = depositService.findByTransactionId(transactionId);
			}while(findDepositByTransactionId != null);
			
			if (paymentMethod.equals("SEPAY")) {
				
				// String paymentUrl = sepayService.createPayment(amount, orderId);
				
				String QR = qrUrl + "?acc=" + bankAccount + "&bank=" + bank + "&amount=" + (amount * rate.getRate()) + "&des=" + transactionId;
				
				// Create deposit record
				Deposit deposit = new Deposit();
				deposit.setAmount(amount);
				deposit.setPaymentMethod("SEPAY");
				deposit.setTransactionId(transactionId);
				deposit.setStatus("WAITING");
				deposit.setUser(currentUser);
				deposit.setQR(QR);
				depositService.save(deposit);
				
				
				// redirectAttributes.addFlashAttribute("paymentUrl", paymentUrl);
				
				if(rate != null) {
					redirectAttributes.addFlashAttribute("rate", rate);
				}
				
				
				return "redirect:/account/deposit/payment/" + deposit.getId();
				
			} else if (paymentMethod.equals("USDT")) {
				// Handle USDT payment
				
				Deposit deposit = new Deposit();
	            deposit.setAmount(amount);
	            deposit.setPaymentMethod("USDT");
	            deposit.setTransactionId(transactionId);
	            deposit.setStatus("WAITING");
	            deposit.setUser(currentUser);
	            depositService.save(deposit);
				
				RestTemplate restTemplate = new RestTemplate();
				String fpaymentApiUrl = usdtApiUrl;
				
				MultiValueMap<String, String> requestData = new LinkedMultiValueMap<>();
				  requestData.add("merchant_id", usdtMerchantId); 
				    requestData.add("api_key", usdtApiKey);
				    requestData.add("name", "Nạp tiền");
				    requestData.add("description", "User: " + currentUser.getEmail());
				    requestData.add("amount", String.valueOf(amount));
				    requestData.add("request_id", transactionId);
				    requestData.add("callback_url",  myDomain + "/api/fpayment/callback");
				    requestData.add("success_url",  myDomain + "/account/deposit/payment/" + deposit.getId());
				    requestData.add("cancel_url",  myDomain + "/account/deposit/payment/" + deposit.getId());
				    
				    HttpHeaders headers = new HttpHeaders();
				    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestData, headers);
				    
				    try {
				        ResponseEntity<String> response = restTemplate.postForEntity(fpaymentApiUrl, requestEntity, String.class);

				        if (response.getStatusCode() == HttpStatus.OK) {
				           
				            ObjectMapper mapper = new ObjectMapper();
				            Map<String, Object> bodyMap = mapper.readValue(response.getBody(), Map.class);

				            if ("success".equals(bodyMap.get("status"))) {
				                Map<String, Object> data = (Map<String, Object>) bodyMap.get("data");
				                String urlPayment = (String) data.get("url_payment");
				                return "redirect:" + urlPayment;
				            } else {
				                redirectAttributes.addFlashAttribute("danger", "Không thể tạo hóa đơn USDT: " + bodyMap.get("msg"));
				                return "redirect:/account/deposit";
				            }
				        } else {
				            redirectAttributes.addFlashAttribute("danger", "Không thể kết nối đến hệ thống tạo hóa đơn.");
				            return "redirect:/account/deposit";
				        }

				    } catch (Exception e) {
				        redirectAttributes.addFlashAttribute("danger", "Lỗi hệ thống khi tạo hóa đơn USDT: " + e.getMessage());
				        return "redirect:/account/deposit";
				    }
				    
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("danger", "Có lỗi xảy ra khi xử lý thanh toán");
			return "redirect:/account/deposit";
		}
		
		redirectAttributes.addFlashAttribute("success", "Yêu cầu nạp tiền đã được ghi nhận!");
		return "redirect:/account/deposit-history";
	}
	

	
	@GetMapping("/account/deposit/payment/{id}")
	public String paymentPage( @PathVariable int id ,   Model model, RedirectAttributes redirectAttributes) {
		Deposit deposit = depositService.findById(id);
		if(id == 0) {
			return "error";
		}
		  
		
		   AdminBank adminBank = adminBankService.findById(1);
			
			if(adminBank != null) {
				model.addAttribute("adminBank", adminBank);
			}
			
		model.addAttribute("deposit", deposit);
		
		Rate rate = rateService.findByCountry("vi");
		model.addAttribute("rate", rate);
		
		return "User/Pages/Account/payment";
	}
}
