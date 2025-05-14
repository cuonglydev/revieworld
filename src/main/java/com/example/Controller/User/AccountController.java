package com.example.Controller.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Entity.User;
import com.example.Service.UserService;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AccountController {
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${my.domain}")
	private String myDomain;

	@GetMapping("/account")
	public String accountPage(Model model) {	
		User currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			return "redirect:/login";
		}
		String inviteLink = myDomain + "/?ref=" + currentUser.getToken(); 
		model.addAttribute("inviteLink", inviteLink);
		
		return "User/Pages/Account/account";
	}
	
	
	@GetMapping("/account/detail")
	public String accountDetailPage(Model model) {
		try {
			User currentUser = userService.getCurrentUser();
			if (currentUser == null) {
				return "redirect:/login";
			}
			
			
			model.addAttribute("currentUser", currentUser);
			return "User/Pages/Account/account-detail";
		} catch (Exception e) {
			logger.error("Error loading account detail page: {}", e.getMessage(), e);
			return "redirect:/error";
		}
	}
	

	@GetMapping("/account/affiliate")
	public String affiliatePage() {
		return "User/Pages/Account/affiliate";
	}
	
	@PostMapping("/account/change-password")
	@ResponseBody
	public ResponseEntity<?> changePassword(
		@RequestParam(value = "oldPassword", required = false) String oldPassword,
		@RequestParam("newPassword") String newPassword) {
		
		User currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			return ResponseEntity.badRequest().body("User not found");
		}

		// Nếu là tài khoản Google (password == null), không cần kiểm tra mật khẩu cũ
		if (currentUser.getPassword() == null) {
			currentUser.setPassword(passwordEncoder.encode(newPassword));
			userService.save(currentUser);
			return ResponseEntity.ok("Password changed successfully");
		}

		// Nếu là tài khoản hệ thống, kiểm tra mật khẩu cũ
		if (oldPassword == null || !passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
			return ResponseEntity.badRequest().body("Invalid old password");
		}

		currentUser.setPassword(passwordEncoder.encode(newPassword));
		userService.save(currentUser);
		return ResponseEntity.ok("Password changed successfully");
	}

	@GetMapping("/account/invite-link")
	@ResponseBody
	public ResponseEntity<?> getInviteLink() {
		User currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			return ResponseEntity.badRequest().body("User not found");
		}

		String inviteCode = currentUser.getInviteCode();
		if (inviteCode == null || inviteCode.isEmpty()) {
			return ResponseEntity.badRequest().body("Invite code not found");
		}

		String inviteLink = "hosting:8080/register?inviteCode=" + inviteCode;
		return ResponseEntity.ok(inviteLink);
	}

	@PostMapping("/account/generate-invite")
	@ResponseBody
	public ResponseEntity<?> generateInviteCode() {
		User currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			return ResponseEntity.badRequest().body("User not found");
		}

		String newInviteCode = UUID.randomUUID().toString();
		currentUser.setInviteCode(newInviteCode);
		userService.save(currentUser);

		return ResponseEntity.ok("New invite code generated successfully");
	}

	@GetMapping("/account/check-login")
	@ResponseBody
	public ResponseEntity<?> checkLogin() {
		User currentUser = userService.getCurrentUser();
		Map<String, Object> response = new HashMap<>();
		
		if (currentUser != null) {
			response.put("loggedIn", true);
			response.put("userId", currentUser.getId());
			response.put("email", currentUser.getEmail());
			return ResponseEntity.ok(response);
		} else {
			response.put("loggedIn", false);
			return ResponseEntity.ok(response);
		}
	}
}
