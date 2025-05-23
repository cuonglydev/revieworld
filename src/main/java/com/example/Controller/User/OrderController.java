package com.example.Controller.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.Service.MissionNoteService;
import com.example.Service.MissionService;
import com.example.Service.OrderService;
import com.example.Service.OrderTypeService;
import com.example.Service.UploadService;
import com.example.Service.UserService;
import com.example.Entity.Mission;
import com.example.Entity.MissionNote;
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
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private MissionNoteService missionNoteService;
	
	@Value("${static-folder}")
	private String staticFolder;
	
	@GetMapping("/order/{slug}")
	public String orderPage( @PathVariable String slug, Model model) {
		User user = userService.getCurrentUser();
		Order order = orderService.findBySlug(slug);
		if(order == null) {
			return "redirect:/404";
		}
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		
		List<Mission> waitingMissions = missionService.findAllByOrderIdAndStatus(order.getId(), "WAITING");
		model.addAttribute("numberWaitingMission", waitingMissions.size());
		model.addAttribute("waitingMissions", waitingMissions);
		
		List<Mission> missions = missionService.findAllByOrderId(order.getId());
		model.addAttribute("missions", missions);
		
		List<Mission> successMissions = missionService.findAllByOrderIdAndStatus(order.getId(), "SUCCESS");
		model.addAttribute("numberSuccessMission", successMissions.size());
		model.addAttribute("successMissions", successMissions);
		
		List<String> statuses = Arrays.asList("REFUSED", "REQUEST_REFUSE", "APPEAL");
		List<Mission> refuseMissions = missionService.findAllByOrderIdAndStatusIn(order.getId(), statuses);
		model.addAttribute("numberRefuseMission", refuseMissions.size());
		model.addAttribute("refuseMissions", refuseMissions);
		
		List<Mission> appealMissions =  missionService.findAllByOrderIdAndStatus(order.getId(), "APPEAL");
		model.addAttribute("numberAppealMission", appealMissions.size());
		model.addAttribute("appealMissions", appealMissions);
		
		return "User/Pages/Order/order-detail";
	}
	
	@GetMapping("/order/mission-detail/{id}")
	public String missionDetailPage(@PathVariable int id, Model model) {
		Mission mission = missionService.findById(id);
		Order order = orderService.findById(mission.getOrder().getId());
		User user = userService.getCurrentUser();
		List<MissionNote> missionNotes = missionNoteService.findAllByMissionId(id);
		if(user.getId() != order.getUser().getId()) {
			return "/404";
		}
		model.addAttribute("mission", mission);
		model.addAttribute("order", order);
		model.addAttribute("missionNotes", missionNotes);
		return "/User/Pages/Order/mission-detail";
	}
	
	@GetMapping("/order/success/{id}")
	public String successMissionFunction(@PathVariable int id, RedirectAttributes redirectAttributes) {
		Mission mission = missionService.findById(id);
		Order order = orderService.findById(mission.getOrder().getId());
		try {
			User user = userService.getCurrentUser();
			if(user.getId() != order.getUser().getId()) {
				return "/404";
			}
			
			mission.setStatus("SUCCESS");
			missionService.save(mission);
			
			order.setQuantityDone(order.getQuantityDone() + 1);
			orderService.save(order);
			user.setBonusAmount(user.getBonusAmount() + mission.getAmount());
			userService.save(user);
			redirectAttributes.addFlashAttribute("success", "Xác nhận thành công!");
		}catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("danger", "Xác nhận thất bại!");
		}
		return "redirect:/order/" + order.getSlug();
	}
	
	@PostMapping("/order/refuse")
	public String refuseOrderFunction(@RequestParam("missionId") int missionId, @RequestParam("note") String note, @RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		Mission mission = missionService.findById(missionId);
		Order order = orderService.findById(mission.getOrder().getId());
		try {
			User user = userService.getCurrentUser();
			if(user.getId() != order.getUser().getId()) {
				return "/404";
			}
			
			mission.setStatus("REQUEST_REFUSE");
			missionService.save(mission);
			
			MissionNote missionNote = new MissionNote();
			missionNote.setNote(note);
			
			if (!file.isEmpty()) {
                String filePhoto = uploadService.saveFile(file, "images"); // Lưu ảnh và lấy tên file
                missionNote.setPhoto(staticFolder + "images/" + filePhoto); 
			}else {
				missionNote.setPhoto(null);
			}
			
			
			redirectAttributes.addFlashAttribute("success", "Từ chối đơn thành công!");
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Không thể từ chối đơn này!");
		}
		return "redirect:/order/" + order.getSlug();
 	}

	@GetMapping("/order/all-order")
	public String allOrderPage(Model model) {
		User user = userService.getCurrentUser();
		List<Order> orders = orderService.findAllByUserId(user.getId());
		model.addAttribute("orders", orders);
		
		List<String> statuses = Arrays.asList("ACTIVE", "PAUSE");
		List<Order> activeOrders = orderService.findAllByUserIdAndStatusIn(user.getId(),  statuses);
		model.addAttribute("activeOrders", activeOrders);
		
		List<Order> pausingOrders = orderService.findAllByUserAndStatus(user.getId(), "REQUEST_PAUSE");
		model.addAttribute("pausingOrders", pausingOrders);
		
		List<Order> closedOrders = orderService.findAllByUserAndStatus(user.getId(), "CLOSED");
		model.addAttribute("closedOrders", closedOrders);
		return "User/Pages/Order/all-order";
	}

//	@GetMapping("/order-detail")
//	public String orderDetailPage() {
//		return "User/Pages/Order/order-detail";
//	}

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
