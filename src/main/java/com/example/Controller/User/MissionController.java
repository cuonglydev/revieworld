package com.example.Controller.User;

import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Mission;
import com.example.Entity.MissionNote;
import com.example.Entity.Order;
import com.example.Entity.OrderContent;
import com.example.Entity.OrderPhoto;
import com.example.Entity.OrderType;
import com.example.Entity.User;
import com.example.Service.MissionNoteService;
import com.example.Service.MissionService;
import com.example.Service.OrderContentService;
import com.example.Service.OrderPhotoService;
import com.example.Service.OrderService;
import com.example.Service.OrderTypeService;
import com.example.Service.UserService;
import org.springframework.web.bind.annotation.RequestBody;


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
	
	@Autowired
	private MissionNoteService missionNoteService;
	
	@Autowired
	private OrderContentService orderContentService;

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

		List<OrderPhoto> activePhotos = orderPhotoService.findAllByOrderId(order.getId()).stream()
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
		
		List<OrderContent> activeContents = orderContentService.findAllByOrderId(order.getId()).stream()
				.filter(content -> "ACTIVE".equals(content.getStatus())).collect(Collectors.toList());

		OrderContent selectedContent = null;
		if (activeContents.size() < order.getQuantity()){
			int totalSlots = order.getQuantity();
			List<OrderContent> tempList = new ArrayList<>(activeContents);
			while (tempList.size() < totalSlots) {
				tempList.add(null);
			}
			Collections.shuffle(tempList);
			selectedContent = tempList.get(0);
		}else {
			Collections.shuffle(activeContents);
			selectedContent = activeContents.get(0);
		}
		model.addAttribute("orderContent", selectedContent);
		
		User user = userService.getCurrentUser();
		Boolean worked = missionService.findByOrderIdAndUserId(order.getId(), user.getId()) != null;
		model.addAttribute("worked", worked);
		return "User/Pages/Mission/mission-detail";
	}

	@PostMapping("/mission/create")
	public String createMissionFunction(@RequestParam("orderSlug") String orderSlug, @RequestParam("url") String url,
			@RequestParam(value = "orderPhotoId", required = false) Integer orderPhotoId,
			@RequestParam(value = "orderContentId", required = false) Integer orderContentId,
			RedirectAttributes redirectAttributes) {
		Order order = orderService.findBySlug(orderSlug);
		User user = userService.getCurrentUser();
		try {
			if ("PAUSE".equals(order.getStatus()) || "REQUEST_PAUSE".equals(order.getStatus())) {
				redirectAttributes.addFlashAttribute("danger", "Nhiệm vụ này đang tạm dừng!");
				return "redirect:/mission";
			}
			
			if("CLOSED".equals(order.getStatus())) {
				redirectAttributes.addFlashAttribute("danger", "Số lượng nhiệm vụ này đã hết!");
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

			Mission existingMission = missionService.findByOrderIdAndUserId(order.getId(), user.getId());
			if (existingMission != null) {
				redirectAttributes.addFlashAttribute("danger", "Bạn đã làm nhiệm vụ này rồi!");
				return "redirect:/mission/" + order.getSlug();
			}

			Mission mission = new Mission();
			mission.setAmount(order.getItemAmount());
			mission.setUrl(url);
			if(orderPhotoId != null) {
				OrderPhoto orderPhoto = orderPhotoService.findById(orderPhotoId);
				mission.setOrderPhoto(orderPhoto);
				orderPhoto.setStatus("DONE");
				orderPhotoService.save(orderPhoto);
			}
			if(orderContentId != null) {
				OrderContent orderContent = orderContentService.findById(orderContentId);
				mission.setOrderContent(orderContent);
				orderContent.setStatus("DONE");
				orderContentService.save(orderContent);
			}
			
			
			mission.setOrder(order);

			Date currentDate = new Date();
			mission.setCreatedAt(currentDate);

			mission.setStatus("WAITING");
			mission.setStatusDate(currentDate);
			mission.setUser(user);
			missionService.save(mission);
			
			int workedQuantity = order.getWorkedQuantity() + 1;

			int turnQuantityDone = order.getTurnQuantityDone() + 1;

			if (turnQuantityDone == order.getTurnQuantity()) {
				order.setStatus("PAUSE");
				order.setLastTurnTime(new Date());
			}
			
			order.setWorkedQuantity(workedQuantity);
			order.setTurnQuantityDone(turnQuantityDone);
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
		
		List<MissionNote> missionNotes = missionNoteService.findAllByMissionId(id);
		model.addAttribute("missionNotes", missionNotes);

		return "User/Pages/Account/mission-history-detail";
	}
	
	@PostMapping("/mission/mission-detail/save")
	public String requestEditFuntion(@RequestParam("missionId") int missionId, @RequestParam(value = "note", required = false) String note,
			@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		//TODO: process POST request
		
		try {
			Mission mission = missionService.findById(missionId);
			User user = userService.getCurrentUser();
			
			if("SUCCESS".equals(mission.getStatus()) || "REQUEST_REFUSE".equals(mission.getStatus()) ||
					"REFUSE".equals(mission.getStatus()) || "APPEAL".equals(mission.getStatus()) || 
					mission.getStatus() == null || "".equals(mission.getStatus())) {
				
		
				redirectAttributes.addFlashAttribute("danger", "Bạn không thể tiếp tục xác thực đơn này!");
				return "redirect:/account/misison-history/" + missionId;
			}
			
			if("REQUEST_EDIT".equals(mission.getStatus())) {
				mission.setStatus("WAITING");
				mission.setStatusDate(new Date());
				missionNoteService.createNewMissionNote(note, file, "EDITED", mission, "WORKER");
				missionService.save(mission);
				redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
			}
			
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Cập nhật thất bại!");
		}
		
		
		
		return "redirect:/account/mission-history/" + missionId;
	}
	
	@GetMapping("/mission/appeal/{id}")
	public String appealMissionFunction(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			User user = userService.getCurrentUser();
			Mission mission = missionService.findById(id);
			if(user.getId() != mission.getUser().getId()) {
				return "/404";
			}
			if("REQUEST_REFUSE".equals(mission.getStatus())) {
				mission.setStatus("APPEAL");
				mission.setStatusDate(new Date());
				missionService.save(mission);
				missionNoteService.createNewMissionNote(null, null, "APPEAL", mission, "WORKER");
				redirectAttributes.addFlashAttribute("success", "Kháng đơn thành công!");
			}
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Không thể kháng đơn!");
			// TODO: handle exception
		}
		return "redirect:/account/mission-history/{id}";
	}
	

//	@GetMapping("/account/mission-history/detail")
//	public String missionHistoryDetailPage() {
//		return "User/Pages/Account/mission-history-detail";
//	}

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
