package com.example.Controller.Admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

	@GetMapping("/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error,
			Model model) {
		if (error != null) {
			model.addAttribute("danger", "Đăng nhập thất bại. Kiểm tra lại thông tin!");
		}
		return "Admin/Auth/login";
	}

}
