package com.example.Controller.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

	@GetMapping("/content")
	public String contentPage() {
		
		return "User/Pages/Content/content";
	}
	
	@GetMapping("/content-detail")
	public String contentDetail() {
		
		return "User/Pages/Content/content-detail";
	}
	
}
