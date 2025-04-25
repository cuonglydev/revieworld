package com.example.Controller.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DepositController {
	
	
	@GetMapping("/account/deposit")
	public String depositPage() {
		return "User/Pages/Account/deposit";
	}

	
	@GetMapping("/account/deposit-history")
	public String depositHistoryPage() {
		return "User/Pages/Account/deposit-history";
	}
	
}
