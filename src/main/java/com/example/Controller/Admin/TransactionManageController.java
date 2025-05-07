package com.example.Controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Service.RankService;

@Controller
@RequestMapping("/admin")
public class TransactionManageController {
	@Autowired
	private RankService rankService;

	@GetMapping("/withdraw")
	public String withdrawPage() {
		return "Admin/Pages/Transaction/withdraw";
	}

}
