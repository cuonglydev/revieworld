package com.example.Controller.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Deposit;
import com.example.Entity.User;
import com.example.Service.DepositService;
import com.example.Service.UserService;
import com.example.Service.SepayService;
import com.example.Service.UsdtService;
import com.example.Service.SystemSettingService;

@Controller
public class DepositController {

	@Autowired
	private DepositService depositService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SepayService sepayService;
	
	@Autowired
	private UsdtService usdtService;
	
	@Autowired
	private SystemSettingService systemSettingService;
	
	@GetMapping("/account/deposit")
	public String depositPage(Model model) {
		User currentUser = userService.getCurrentUser();
		if (currentUser != null) {
			model.addAttribute("currentBalance", currentUser.getAmount());
			model.addAttribute("minDeposit", systemSettingService.getMinDeposit());
			model.addAttribute("maxDeposit", systemSettingService.getMaxDeposit());
			model.addAttribute("usdToVndRate", systemSettingService.getUsdToVndRate());
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
							   RedirectAttributes redirectAttributes) {
		User currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			redirectAttributes.addFlashAttribute("danger", "Bạn cần đăng nhập!");
			return "redirect:/account/deposit";
		}
		
		// Validate against system settings
		Double minDeposit = systemSettingService.getMinDeposit();
		Double maxDeposit = systemSettingService.getMaxDeposit();
		
		if (amount == null || amount < minDeposit) {
			redirectAttributes.addFlashAttribute("danger", "Số tiền nạp tối thiểu là $" + minDeposit);
			return "redirect:/account/deposit";
		}
		
		if (amount > maxDeposit) {
			redirectAttributes.addFlashAttribute("danger", "Số tiền nạp tối đa là $" + maxDeposit);
			return "redirect:/account/deposit";
		}
		
		if (!paymentMethod.equals("SEPAY") && !paymentMethod.equals("USDT")) {
			redirectAttributes.addFlashAttribute("danger", "Phương thức thanh toán không hợp lệ");
			return "redirect:/account/deposit";
		}

		try {
			if (paymentMethod.equals("SEPAY")) {
				String orderId = "ORDER" + System.currentTimeMillis();
				String returnUrl = "http://localhost:8080/account/deposit";
				String paymentUrl = sepayService.createPayment(amount, orderId, returnUrl);
				
				// Create deposit record
				Deposit deposit = new Deposit();
				deposit.setAmount(amount);
				deposit.setPaymentMethod("SEPAY");
				deposit.setTransactionId(orderId);
				deposit.setStatus("PENDING");
				deposit.setUser(currentUser);
				depositService.save(deposit);
				
				redirectAttributes.addFlashAttribute("paymentUrl", paymentUrl);
				return "redirect:/account/deposit-waiting";
			} else if (paymentMethod.equals("USDT")) {
				// Handle USDT payment
				String usdtAddress = usdtService.createPayment(amount, currentUser);
				redirectAttributes.addFlashAttribute("usdtAddress", usdtAddress);
				redirectAttributes.addFlashAttribute("usdtAmount", amount);
				return "redirect:/account/deposit-usdt";
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("danger", "Có lỗi xảy ra khi xử lý thanh toán");
			return "redirect:/account/deposit";
		}
		
		redirectAttributes.addFlashAttribute("success", "Yêu cầu nạp tiền đã được ghi nhận!");
		return "redirect:/account/deposit-history";
	}
}
