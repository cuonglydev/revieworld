package com.example.Controller.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.User;
import com.example.Entity.Withdraw;
import com.example.Entity.UserBank;
import com.example.Service.UserService;
import com.example.Service.WithdrawService;
import com.example.Service.UserBankService;
import java.util.List;
import java.util.ArrayList;

@Controller
public class WithdrawController {

	@Autowired
	private WithdrawService withdrawService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserBankService userBankService;
	
	@GetMapping("/account/withdraw")
	public String withdrawPage(Model model) {
		User currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			return "redirect:/login";
		}
		
		// Add user's bank accounts to model
		model.addAttribute("userBanks", userBankService.findByUserId(currentUser.getId()));
		model.addAttribute("bonusBalance", currentUser.getBonusAmount());
		
		return "User/Pages/Account/withdraw";
	}
	
	
	@GetMapping("/account/withdraw-history")
	public String withdrawHistoryPage(Model model) {
		User currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			return "redirect:/login";
		}
		List<Withdraw> withdraws = withdrawService.findByUserId(currentUser.getId());
		model.addAttribute("withdraws", withdraws);
		
		return "User/Pages/Account/withdraw-history";
	}
	
	@PostMapping("/account/withdraw")
	public String processWithdraw(@RequestParam("amount") Double amount,
								@RequestParam("bankId") int bankId,
								RedirectAttributes redirectAttributes) {
		User currentUser = userService.getCurrentUser();
		
		// Validate amount
		if (amount == null || amount < 5 || amount > 500) {
			redirectAttributes.addFlashAttribute("danger", "Số tiền rút phải từ $5 đến $500");
			return "redirect:/account/withdraw";
		}
		
		// Check if user has enough bonus balance
		if (currentUser.getBonusAmount() < amount) {
			redirectAttributes.addFlashAttribute("danger", "Số dư không đủ");
			return "redirect:/account/withdraw";
		}
		
		// Get selected bank account
		UserBank userBank = userBankService.findById(bankId)
			.orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại"));
		
		// Verify ownership
		if (userBank.getUser().getId() != currentUser.getId()) {
			redirectAttributes.addFlashAttribute("error", "Tài khoản không hợp lệ");
			return "redirect:/account/withdraw";
		}
		
		// Create new withdraw request
		Withdraw withdraw = new Withdraw();
		withdraw.setAmount(amount);
		withdraw.setBankName(userBank.getBankName());
		withdraw.setAccountName(userBank.getAccountName());
		withdraw.setAccountNumber(userBank.getAccountNumber());
		withdraw.setUserBankMethod(userBank.getMethod());
		withdraw.setStatus("PENDING");
		withdraw.setCreateAt(new java.util.Date());
		withdraw.setUser(currentUser);
		
		// Save withdraw request
		withdrawService.save(withdraw);
		
		// Update user's bonus balance
		currentUser.setBonusAmount(currentUser.getBonusAmount() - amount);
		userService.save(currentUser);
		
		redirectAttributes.addFlashAttribute("success", "Yêu cầu rút tiền đã được gửi thành công");
		return "redirect:/account/withdraw-history";
	}
}
