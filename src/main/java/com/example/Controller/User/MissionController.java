package com.example.Controller.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Service.MissionService;

@Controller
public class MissionController {

	@Autowired
	private MissionService missionService;

	@GetMapping("/mission")
	public String missonPage() {
		return "User/Pages/Mission/mission";
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
