package com.example.Controller.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.example.Service.MissionService;
import com.example.Service.OrderService;
import com.example.Service.OrderTypeService;
import com.example.Service.UserService;
import com.example.Entity.Mission;
import com.example.Entity.Order;
import com.example.Entity.OrderType;
import com.example.Entity.User;

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderTypeService orderTypeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MissionService missionService;
	
	@GetMapping("/order/{slug}")
	public String orderPage( @PathVariable String slug, Model model) {
		User user = userService.getCurrentUser();
		Order order = orderService.findBySlug(slug);
		if(order == null) {
			return "redirect:/404";
		}
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		
		List<Mission> missions = missionService.findAllByOrderIdAndStatus(order.getId(), "WAITING");
		model.addAttribute("numberWaitingMission", missions.size());
		
		return "User/Pages/Order/order-detail";
	}

	@GetMapping("/all-order")
	public String allOrderPage() {
		return "User/Pages/Order/all-order";
	}

	@GetMapping("/order-detail")
	public String orderDetailPage() {
		return "User/Pages/Order/order-detail";
	}

//	// Xử lý tạo đơn hàng
//	@PostMapping("/create-order")
//	public String createOrder(@RequestParam int userId, @RequestParam String name, @RequestParam String url,
//			@RequestParam String description, @RequestParam String language, @RequestParam String slug,
//			@RequestParam Double totalAmount, Model model) {
//
//		Order newOrder = orderService.createOrder(userId, name, url, description, language, slug, totalAmount);
//
//		// Thêm đơn hàng vào model và chuyển hướng tới trang chi tiết đơn hàng
//		model.addAttribute("order", newOrder);
//		return "User/Pages/Order/order-detail"; // Chuyển hướng tới trang chi tiết đơn
//	}
//
//	@GetMapping("/pay-order/{orderId}")
//	public String payOrder(@PathVariable int orderId, Model model) {
//		Order updatedOrder = orderService.processOrderPayment(orderId);
//
//		// Thêm thông tin đơn hàng đã thanh toán vào model
//		model.addAttribute("order", updatedOrder);
//		model.addAttribute("message", "Order paid successfully. Discount applied: " + updatedOrder.getTotalAmount());
//
//		return "User/Pages/Order/payment-result"; // Chuyển hướng đến trang kết quả thanh toán
//	}

}
