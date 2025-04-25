package com.example.Controller.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WithdrawController {


	@GetMapping("/account/withdraw")
	public String withdrawPage() {
		return "User/Pages/Account/withdraw";
	}
	
	
	@GetMapping("/account/withdraw-history")
	public String withdrawHistoryPage() {
		return "User/Pages/Account/withdraw-history";
	}
	
	
}
