package com.example.Controller.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MissionController {

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
	
}
