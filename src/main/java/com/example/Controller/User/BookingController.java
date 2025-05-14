package com.example.Controller.User;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Entity.Order;
import com.example.Entity.OrderType;
import com.example.Entity.User;
import com.example.Service.OrderTypeService;
import com.example.Service.UserService;

@Controller
public class BookingController {
	
	@Autowired
	OrderTypeService orderTypeService;
	
	@Autowired
	UserService userService;

	@GetMapping("/booking")
	public String bookingPage(Model model) {
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);
		
		List<OrderType> orderTypes = orderTypeService.getAllOrderTypes();
		model.addAttribute("orderTypes", orderTypes);
		return "User/Pages/Booking/booking";
	}
	
	@GetMapping("/booking/{slug}")
	public String bookingDetailPage(@PathVariable String slug, Model model) {
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);
		
		OrderType orderType = orderTypeService.findBySlug(slug);
		model.addAttribute("orderType", orderType);
		return "User/Pages/Booking/booking-detail";
	}
	
	/*
	 * @PostMapping("/booking/create") public String createBooking(@ModelAttribute
	 * Order order) { User user = userService.getCurrentUser(); Date currentDate =
	 * new Date(); try {
	 * 
	 * order }
	 * 
	 * return entity; }
	 * 
	 */
	

}
