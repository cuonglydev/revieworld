package com.example.Controller.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AccountController {

	@GetMapping("/account")
	public String accountPage() {
		return "User/Pages/Account/account";
	}
	
	@GetMapping("/account/detail")
	public String accountDetailPage() {
		return "User/Pages/Account/account-detail";
	}
	

	@GetMapping("/account/affiliate")
	public String affiliatePage() {
		return "User/Pages/Account/affiliate";
	}
	

	
	
}
