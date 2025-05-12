package com.example.Controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Entity.Withdraw;
import com.example.Service.RankService;
import com.example.Service.WithdrawService;

import jakarta.validation.constraints.AssertFalse.List;

@Controller
@RequestMapping("/admin")
public class TransactionManageController {
	@Autowired
	private RankService rankService;

	@Autowired
	WithdrawService withdrawService;
	
	@GetMapping("/withdraw")
	public String withdrawPage(Model model) {
		
		return "Admin/Pages/Transaction/withdraw";
	}

}
