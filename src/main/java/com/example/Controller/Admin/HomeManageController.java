package com.example.Controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class HomeManageController {

	@GetMapping("/index")
	public String indexPage() {
		return "Admin/index";
	}
	
	
}
