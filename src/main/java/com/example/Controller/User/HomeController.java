package com.example.Controller.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.Repository.PopupsRepository;
import java.util.List;
import com.example.Entity.Popups;
import com.example.Service.PopupsService;
import java.util.Optional;

@Controller
public class HomeController {

	@Autowired
	private PopupsRepository popupsRepository;

	@Autowired
	private PopupsService popupsService;

	@GetMapping({"/index", "", "/"})
	public String home(Model model) {
		Optional<Popups> popupOpt = popupsService.getLatestActivePopup();
		model.addAttribute("popup", popupOpt.orElse(null));
		return "User/index";
	}
	
}
