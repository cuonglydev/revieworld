package com.example.Controller.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import com.example.Repository.PopupRepository;

@Controller
public class HomeController {

//	@Autowired
//	private PopupRepository popupRepository;

	@GetMapping({"/index", "", "/"})
	public String home(Model model) {
//		model.addAttribute("activePopups", popupRepository.findByActiveTrue());
		return "User/index";
	}
	
}
