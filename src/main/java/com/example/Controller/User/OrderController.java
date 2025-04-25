package com.example.Controller.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

	@GetMapping("/all-order")
	public String allOrderPage() {
		return "User/Pages/Order/all-order";
	}
	
	@GetMapping("/order-detail")
	public String orderDetailPage() {
		return "User/Pages/Order/order-detail";
	}
	
}
