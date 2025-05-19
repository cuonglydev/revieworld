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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Mission;
import com.example.Entity.Order;
import com.example.Entity.OrderType;
import com.example.Entity.User;
import com.example.Service.MissionService;
import com.example.Service.OrderService;
import com.example.Service.OrderTypeService;
import com.example.Service.UserService;

@Controller
public class MissionController {

	@Autowired
	private MissionService missionService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderTypeService orderTypeService;
	
	@Autowired
	private UserService userService;
	

	@GetMapping("/mission")
	public String missonPage(Model model) {
		List<Order> orders = orderService.findAll();
		model.addAttribute("orders", orders);
		List<OrderType> orderTypes = orderTypeService.getAllOrderTypes();
		model.addAttribute("orderTypes", orderTypes);
		return "User/Pages/Mission/mission";
	}
	
	@GetMapping("/mission/{slug}")
	public String missionDetailPage(@PathVariable String slug, Model model) {
		Order order = orderService.findBySlug(slug);
		model.addAttribute("order", order);
		return "User/Pages/Mission/mission-detail";
	}
	
	@PostMapping("/mission/create")
	public String createMissionFunction(@RequestParam("amount") Double amount, @RequestParam("orderId") int orderId, @RequestParam("url") String url,
			RedirectAttributes redirectAttributes) {
		Order order = orderService.findById(orderId);
		try {
			User user = userService.getCurrentUser();
			Mission mission = new Mission();
			mission.setAmount(amount);
			mission.setUrl(url);
			
			
			mission.setOrder(order);
			
			Date currentDate = new Date();
			mission.setCreatedAt(currentDate);
			
			mission.setStatus("WAITING");
			mission.setStatusDate(currentDate);
			mission.setUser(user);
			missionService.save(mission);
			
			redirectAttributes.addFlashAttribute("success", "Làm nhiệm vụ thành công!");
			return "redirect:/account/mission-history/" + mission.getId();
		}catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("danger", "Làm nhiệm vụ thất bại!");
			return "redirect:/misison/" + order.getSlug();
		}
		
	}

	@GetMapping("/mission-detail")
	public String missonDetailPage() {
		return "User/Pages/Mission/mission-detail";
	}

	@GetMapping("/account/mission-history")
	public String missionHistoryPage() {
		return "User/Pages/Account/mission-history";
	}

	@GetMapping("/account/mission-history/detail")
	public String missionHistoryDetailPage() {
		return "User/Pages/Account/mission-history-detail";
	}

	// Bắt đầu làm nhiệm vụ (Trạng thái: WAITING)
	@GetMapping("/start-mission/{missionId}")
	public String startMission(@PathVariable int missionId) {
		missionService.startMission(missionId);
		return "redirect:/missionDetail/" + missionId;
	}

	// Duyệt nhiệm vụ (Trạng thái: APPROVED)
	@GetMapping("/approve-mission/{missionId}")
	public String approveMission(@PathVariable int missionId) {
		missionService.approveMission(missionId);
		return "redirect:/missionDetail/" + missionId;
	}

	// Từ chối nhiệm vụ (Trạng thái: REJECTED)
	@GetMapping("/reject-mission/{missionId}")
	public String rejectMission(@PathVariable int missionId) {
		missionService.rejectMission(missionId);
		return "redirect:/missionDetail/" + missionId;
	}

	// Người dùng yêu cầu chỉnh sửa nhiệm vụ
	@GetMapping("/request-edit-mission/{missionId}")
	public String requestEditMission(@PathVariable int missionId) {
		missionService.requestEditMission(missionId);
		return "redirect:/missionDetail/" + missionId;
	}

}
