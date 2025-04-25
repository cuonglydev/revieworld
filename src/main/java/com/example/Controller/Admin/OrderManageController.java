package com.example.Controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/admin")
public class OrderManageController {

	@GetMapping("/order-type")
	public String orderTypePage() {
		return "Admin/Pages/Order/order-type";
	}
	
	@GetMapping("/order")
	public String orderPage() {
		return "Admin/Pages/Order/order";
	}
	
	@GetMapping("/appeal-mission")
	public String appealMission() {
		return "Admin/Pages/Order/appeal-mission";
	}
	
}
