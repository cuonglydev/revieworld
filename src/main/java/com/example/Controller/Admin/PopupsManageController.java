package com.example.Controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/admin")
public class PopupsManageController {

	@GetMapping("/popups")
	public String popupsPage() {
		return "Admin/Pages/Popups/popups";
	}
	
}
