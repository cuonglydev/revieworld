package com.example.Controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/admin")
public class UserManageController {

	@GetMapping("/user")
	public String userPage() {
		return "Admin/Pages/User/user";
	}
	
	@GetMapping("/rank")
	public String rankPage() {
		return "Admin/Pages/User/rank";
	}
	
}
