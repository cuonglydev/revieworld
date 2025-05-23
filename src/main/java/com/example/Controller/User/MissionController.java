package com.example.Controller.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.example.Entity.OrderPhoto;
import com.example.Entity.OrderType;
import com.example.Entity.User;
import com.example.Service.MissionService;
import com.example.Service.OrderPhotoService;
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

	@Autowired
	private OrderPhotoService orderPhotoService;

	@GetMapping("/mission")
	public String missonPage(Model model) {
		List<Order> orders = orderService.findAll();
		model.addAttribute("orders", orders);
		List<OrderType> orderTypes = orderTypeService.getAllOrderTypes();
		model.addAttribute("orderTypes", orderTypes);
		User user = userService.getCurrentUser();

		Map<Integer, Boolean> workedMap = new HashMap<>();
		for (Order order : orders) {
			Mission mission = missionService.findByOrderIdAndUserId(order.getId(), user.getId());
			if (mission != null) {
				workedMap.put(order.getId(), true);
			} else {
				workedMap.put(order.getId(), false);
			}
		}
		model.addAttribute("workedMap", workedMap);
		model.addAttribute("user", user);
		return "User/Pages/Mission/mission";
	}

	@GetMapping("/mission/{slug}")
	public String missionDetailPage(@PathVariable String slug, Model model) {
		Order order = orderService.findBySlug(slug);
		model.addAttribute("order", order);

		List<OrderPhoto> activePhotos = orderPhotoService.findAllByIOrderId(order.getId()).stream()
				.filter(photo -> "ACTIVE".equals(photo.getStatus())).collect(Collectors.toList());

		OrderPhoto selectedPhoto = null;

		if (activePhotos.size() < order.getQuantity()) {
		    int totalSlots = order.getQuantity();
		    List<OrderPhoto> tempList = new ArrayList<>(activePhotos);
		    while (tempList.size() < totalSlots) {
		        tempList.add(null);
		    }
		    Collections.shuffle(tempList);
		    selectedPhoto = tempList.get(0);
		} else {
		    
		    Collections.shuffle(activePhotos);
		    selectedPhoto = activePhotos.get(0);
		}

		model.addAttribute("orderPhoto", selectedPhoto);

		User user = userService.getCurrentUser();
		Boolean worked = missionService.findByOrderIdAndUserId(order.getId(), user.getId()) != null;
		model.addAttribute("worked", worked);
		return "User/Pages/Mission/mission-detail";
	}

	@PostMapping("/mission/create")
	public String createMissionFunction(@RequestParam("orderId") int orderId, @RequestParam("url") String url, @RequestParam(value = "orderPhoto", required = false) String orderPhoto,
			RedirectAttributes redirectAttributes) {
		Order order = orderService.findById(orderId);
		User user = userService.getCurrentUser();
		try {
			if ("PAUSE".equals(order.getStatus())) {
				redirectAttributes.addFlashAttribute("danger", "Nhiệm vụ này đang tạm dừng!");
				return "redirect:/mission";
			}
			if (order.getTurnQuantityDone() == order.getTurnQuantity()) {
				redirectAttributes.addFlashAttribute("danger",
						"Số lượng đơn trong ngày của nhiệm vụ này đã đầy, hãy quay lại vào ngày mai!");
				return "redirect:/mission";
			}
			if (order.getWorkedQuantity() == order.getQuantity()) {
				redirectAttributes.addFlashAttribute("danger", "Số lượng đơn của nhiệm vụ này đã đầy!");
			}

			Mission existingMission = missionService.findByOrderIdAndUserId(orderId, user.getId());
			if (existingMission != null) {
				redirectAttributes.addFlashAttribute("danger", "Bạn đã làm nhiệm vụ này rồi!");
				return "redirect:/mission/" + order.getSlug();
			}

			Mission mission = new Mission();
			mission.setAmount(order.getItemAmount());
			mission.setUrl(url);
			mission.setOrderPhoto(orderPhoto);
			
			mission.setOrder(order);

			Date currentDate = new Date();
			mission.setCreatedAt(currentDate);

			mission.setStatus("WAITING");
			mission.setStatusDate(currentDate);
			mission.setUser(user);
			missionService.save(mission);

			int turnQuantityDone = order.getTurnQuantityDone() + 1;

			if (turnQuantityDone == order.getTurnQuantity()) {
				order.setStatus("PAUSE");
				order.setLastTurnTime(new Date());
			}
			order.setTurnQuantityDone(turnQuantityDone);

			int workedQuantity = order.getWorkedQuantity() + 1;
			order.setWorkedQuantity(workedQuantity);

			orderService.save(order);

			redirectAttributes.addFlashAttribute("success", "Làm nhiệm vụ thành công!");
			return "redirect:/account/mission-history/" + mission.getId();
		} catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("danger", "Làm nhiệm vụ thất bại!");
			return "redirect:/misison/" + order.getSlug();
		}

	}

	@GetMapping("/account/mission-history")
	public String missionHistoryPage(Model model) {
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);
		List<Mission> missions = missionService.findAllByUserId(user.getId());
		model.addAttribute("missions", missions);
		return "User/Pages/Account/mission-history";
	}

	@GetMapping("/account/mission-history/{id}")
	public String getMissionHistory(@PathVariable int id, Model model) {
		Mission mission = missionService.findById(id);
		User user = userService.getCurrentUser();

		if (user.getId() != mission.getUser().getId()) {
			return "/404";
		}

		model.addAttribute("mission", mission);

		return "User/Pages/Account/mission-history-detail";
	}

	@GetMapping("/account/mission-history/detail")
	public String missionHistoryDetailPage() {
		return "User/Pages/Account/mission-history-detail";
	}

//	// Bắt đầu làm nhiệm vụ (Trạng thái: WAITING)
//	@GetMapping("/start-mission/{missionId}")
//	public String startMission(@PathVariable int missionId) {
//		missionService.startMission(missionId);
//		return "redirect:/missionDetail/" + missionId;
//	}
//
//	// Duyệt nhiệm vụ (Trạng thái: APPROVED)
//	@GetMapping("/approve-mission/{missionId}")
//	public String approveMission(@PathVariable int missionId) {
//		missionService.approveMission(missionId);
//		return "redirect:/missionDetail/" + missionId;
//	}
//
//	// Từ chối nhiệm vụ (Trạng thái: REJECTED)
//	@GetMapping("/reject-mission/{missionId}")
//	public String rejectMission(@PathVariable int missionId) {
//		missionService.rejectMission(missionId);
//		return "redirect:/missionDetail/" + missionId;
//	}
//
//	// Người dùng yêu cầu chỉnh sửa nhiệm vụ
//	@GetMapping("/request-edit-mission/{missionId}")
//	public String requestEditMission(@PathVariable int missionId) {
//		missionService.requestEditMission(missionId);
//		return "redirect:/missionDetail/" + missionId;
//	}

}
