package com.example.Controller.Admin;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.ui.Model;
import org.springframework.util.comparator.Comparators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Mission;
import com.example.Entity.MissionNote;
import com.example.Entity.Order;
import com.example.Entity.OrderPhoto;
import com.example.Entity.OrderType;
import com.example.Entity.User;
import com.example.Service.MissionNoteService;
import com.example.Service.MissionService;
import com.example.Service.OrderPhotoService;
import com.example.Service.OrderService;
import com.example.Service.OrderTypeService;
import com.example.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class OrderManageController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderTypeService orderTypeService;

	@Autowired
	private MissionService missionService;

	@Autowired
	private UserService userService;

	@Autowired
	private MissionNoteService missionNoteService;

	private final OrderPhotoService orderPhotoService;

	public OrderManageController(OrderPhotoService orderPhotoService) {
		this.orderPhotoService = orderPhotoService;
	}

	@GetMapping("/order-type")
	public String orderTypePage(Model model) {
		List<OrderType> orderTypes = orderTypeService.getAllOrderTypes();
		model.addAttribute("orderTypes", orderTypes);
		return "Admin/Pages/Order/order-type";
	}

	@GetMapping("/order")
	public String orderPage(Model model) {
		List<Order> orders = orderService.findAll();
		orders.sort(Comparator.comparing(Order::getCreatedAt).reversed());
		model.addAttribute("orders", orders);

		return "Admin/Pages/Order/order";
	}

	@GetMapping("/order/{id}")
	public String getOrderPage(@PathVariable int id, Model model) {
		Order order = orderService.findById(id);
		model.addAttribute("order", order);

		List<OrderPhoto> orderPhotos = orderPhotoService.findAllByOrderId(id);
		model.addAttribute("orderPhotos", orderPhotos);
		

		List<Mission> waitingMissions = missionService.findAllByOrderIdAndStatus(order.getId(), "WAITING");
		waitingMissions.sort(Comparator.comparing(Mission::getCreatedAt).reversed());
		model.addAttribute("numberWaitingMission", waitingMissions.size());
		model.addAttribute("waitingMissions", waitingMissions);

		List<Mission> requestEditMissions = missionService.findAllByOrderIdAndStatus(order.getId(), "REQUEST_EDIT");
		requestEditMissions.sort(Comparator.comparing(Mission::getCreatedAt).reversed());
		model.addAttribute("numberRequestEditMission", requestEditMissions.size());
		model.addAttribute("requestEditMissions", requestEditMissions);

		List<Mission> missions = missionService.findAllByOrderId(order.getId());
		missions.sort(Comparator.comparing(Mission::getCreatedAt).reversed());
		model.addAttribute("missions", missions);

		List<Mission> successMissions = missionService.findAllByOrderIdAndStatus(order.getId(), "SUCCESS");
		successMissions.sort(Comparator.comparing(Mission::getCreatedAt).reversed());
		model.addAttribute("numberSuccessMission", successMissions.size());
		model.addAttribute("successMissions", successMissions);

		List<String> statuses = Arrays.asList("REFUSED", "REQUEST_REFUSE", "APPEAL");
		List<Mission> refuseMissions = missionService.findAllByOrderIdAndStatusIn(order.getId(), statuses);
		refuseMissions.sort(Comparator.comparing(Mission::getCreatedAt).reversed());
		model.addAttribute("numberRefuseMission", refuseMissions.size());
		model.addAttribute("refuseMissions", refuseMissions);

		List<Mission> appealMissions = missionService.findAllByOrderIdAndStatus(order.getId(), "APPEAL");
		appealMissions.sort(Comparator.comparing(Mission::getCreatedAt).reversed());
		model.addAttribute("numberAppealMission", appealMissions.size());
		model.addAttribute("appealMissions", appealMissions);

		List<String> expectedStatuses = Arrays.asList("WAITING", "REQUEST_EDIT");
		List<Mission> expectedMissions = missionService.findAllByOrderIdAndStatusIn(order.getId(), expectedStatuses);
		model.addAttribute("numberExpectedMissions", expectedMissions.size());

		return "/Admin/Pages/Order/order-detail";
	}

	@PostMapping("/order/mission-detail/save")
	public String saveMissionDetail(@RequestParam("missionId") int missionId, @RequestParam("status") String status,
			@RequestParam(value = "note", required = false) String note,
			@RequestParam(value = "file", required = false) MultipartFile file, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		try {
			Mission mission = missionService.findById(missionId);
			Order order = orderService.findById(mission.getOrder().getId());
			User missionUser = userService.findById(mission.getUser().getId());

			if (mission.getStatus() == null || mission.getStatus().isEmpty()
					|| Set.of("SUCCESS", "REQUEST_REFUSE", "REFUSE", "APPEAL").contains(mission.getStatus())) {

				redirectAttributes.addFlashAttribute("danger", "Bạn không thể tiếp tục xác thực đơn này!");
				return "redirect:" + (referer != null ? referer : "/");
			}

			missionService.handleMissionStatusChange(mission, order, missionUser, status, note, file, "OWNER",
					redirectAttributes);

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Thực hiện chức năng thất bại!");
		}

		return "redirect:" + (referer != null ? referer : "/");
	}

	@PostMapping("/order/mission/appeal/save")
	public String appealMission(@RequestParam("missionId") int missionId, @RequestParam("status") String status,
			@RequestParam(value = "note", required = false) String note,
			@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String referer = request.getHeader("Referer");
		try {
			Mission mission = missionService.findById(missionId);
			Order order = orderService.findById(mission.getOrder().getId());
			User missionUser = userService.findById(mission.getUser().getId());

			missionService.handleMissionStatusChange(mission, order, missionUser, status, note, file, "ADMIN",
					redirectAttributes);


		} catch (Exception e) {
			// TODO: handle exception
		}
		return "redirect:" + (referer != null ? referer : "/");
	}

	
	@GetMapping("/order/mission-detail/{id}")
	public String missionDetailPage(@PathVariable int id, Model model) {
		Mission mission = missionService.findById(id);
		model.addAttribute("mission", mission);

		Order order = orderService.findById(mission.getOrder().getId());
		List<MissionNote> missionNotes = missionNoteService.findAllByMissionId(id);

		model.addAttribute("order", order);
		model.addAttribute("missionNotes", missionNotes);
		
		return "/Admin/Pages/Order/mission-detail";
	}
	
	
	@GetMapping("/order/request-pause/{slug}")
	public String pauseOrderFunction(@PathVariable String slug, RedirectAttributes redirectAttributes) {
		Order order = orderService.findBySlug(slug);
		try {
		
			
			if("PAUSE".equals(order.getStatus()) || "ACTIVE".equals(order.getStatus())) {
				order.setStatus("REQUEST_PAUSE");
				orderService.save(order);
				redirectAttributes.addFlashAttribute("success", "Tạm dừng nhận review thành công!");
			}
			if("CLOSED".equals(order.getStatus())) {
				redirectAttributes.addFlashAttribute("danger", "Đơn hàng này đã đóng!");
			}
		}catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("danger", "Tạm dừng nhận reivew thất bại!");
		}
		return "redirect:/admin/order/" + order.getId();
	}
	
	@GetMapping("/order/request-active/{slug}")
	public String activeOrderFunction(@PathVariable String slug, RedirectAttributes redirectAttributes) {
		Order order = orderService.findBySlug(slug);
		try {
			
			if("REQUEST_PAUSE".equals(order.getStatus())) {
				order.setStatus("ACTIVE");
				orderService.save(order);
				redirectAttributes.addFlashAttribute("success", "Mở nhận review thành công!");
			}
			if("CLOSED".equals(order.getStatus())) {
				redirectAttributes.addFlashAttribute("danger", "Đơn hàng này đã đóng!");
			}
		}catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("danger", "Mở nhận review thất bại!");
		}
		return "redirect:/admin/order/" + order.getId();
	}
}
