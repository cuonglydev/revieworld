package com.example.Controller.Admin;

import java.util.List;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Entity.OrderType;
import com.example.Service.OrderTypeService;

@Controller
@RequestMapping("/admin")
public class OrderManageController {

	@Autowired
	private OrderTypeService orderTypeService;

	@GetMapping("/order-type")
	public String orderTypePage(Model model) {
		List<OrderType> orderTypes = orderTypeService.getAllOrderTypes();
		model.addAttribute("orderTypes", orderTypes);
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
