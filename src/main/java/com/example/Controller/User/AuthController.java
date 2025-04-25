package com.example.Controller.User;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Common.EmailService;
import com.example.Common.TokenGenerator;
import com.example.Entity.DefaultRank;
import com.example.Entity.User;
import com.example.Entity.UserAffiliate;
import com.example.Service.DefaultRankService;
import com.example.Service.UserAffiliateService;
import com.example.Service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAffiliateService userAffiliateService;
	
	@Autowired
	private DefaultRankService defaultRankService;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@Autowired
	private EmailService emailService;
	
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	
	@GetMapping("/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error,
	                        Model model) {
	    if (error != null) {
	        model.addAttribute("danger", "Đăng nhập thất bại. Kiểm tra lại thông tin!");
	    }
		return "User/Auth/login";
	}
	
	@GetMapping("/register")
	public String registerPage() {
		return "User/Auth/register";
	}
	
	@PostMapping("/register")
	public String register(@RequestParam("email") String email, @RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam(value = "inviteCode", required = false) String inviteCode, RedirectAttributes redirectAttributes, Model model) {

		try { 
			
			User user = new User();
			
			User userByEmail = userService.findByEmail(email);
			
			if(userByEmail != null) {
				if(userByEmail.getStatus().equals("NOT-CONFIRMED")) {
					userService.deleteById(userByEmail.getId());
				}else {
					redirectAttributes.addFlashAttribute("danger", "Email này đã được đăng ký!");
					return "redirect:/register";
				}
			}
			
			user.setEmail(email);
			
			if(inviteCode != null && !inviteCode.isEmpty()) {
				User userByInviteCode = userService.findByInviteCode(inviteCode);
				if(userByInviteCode == null) {
					redirectAttributes.addFlashAttribute("danger", "Không tìm thấy mã mời!");
					return "redirect:/register";
				}else {
					UserAffiliate userAffiliate = new UserAffiliate();
					userAffiliate.setAmount(0.0);
					userAffiliate.setUser(userByInviteCode);
					userAffiliateService.save(userAffiliate);
					
					user.setUserAffiliate(userAffiliate);
					
				}
			}
			
			user.setUsername(username);
			
			String encodedPassword = passwordEncoder.encode(password);
		    user.setPassword(encodedPassword);
			
			user.setAmount(0.0);
			user.setBonusAmount(0.0);
			
			Date currentDate = new Date();
			user.setCreatedAt(currentDate);
			
			user.setProvider("SYSTEM");
			user.setProviderId(null);
			
			DefaultRank defaultRank = defaultRankService.findById(1);
			if(defaultRank.isStatus()) {
				if(defaultRank.getRank() != null) {
					user.setRank(defaultRank.getRank());
				}
			}
			
			user.setStatus("NOT-CONFIRMED");
	
			String token = tokenGenerator.generateToken();
			user.setToken(token);
			
			String randomInviteCode = tokenGenerator.generateInviteCode();
			user.setInviteCode(randomInviteCode);
			
			userService.save(user);
			
			String subject = "Xác thực tài khoản";
			String text = "Mã token của bạn là:" + token;
			
			try {
				emailService.sendEmail(email, subject, text);
				redirectAttributes.addFlashAttribute("success", "Đã gửi mã token đến địa chỉ email của bạn!");
				
				return "redirect:/register-token?userId=" + user.getId();
			}catch (Exception e) {
				 redirectAttributes.addFlashAttribute("danger", "Địa chỉ email không tồn tại hoặc không hợp lệ!");
				return "redirect:/register";
			}
			
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Lỗi không thể tạo tài khoản!");
			return  "redirect:/register";
		}
		
		
	}
	
	@GetMapping("/register-token")
	public String confirmTokenPage(@RequestParam("userId") int userId, Model model) {
	    User user = userService.findById(userId);
	    if (user == null || !user.getStatus().equals("NOT-CONFIRMED")) {
	        return "redirect:/register"; 
	    }
	    model.addAttribute("userId", user.getId());
	    return "User/Auth/register-token";
	}
	
	@PostMapping("/register/confirm-token")
	public String confirmTokenRegister(@RequestParam("userId") int userId, @RequestParam("token") String token, RedirectAttributes redirectAttributes) {
		try {
			User user = userService.findById(userId);
			if(token.equals(user.getToken())) {
				user.setStatus("ACTIVE");
				user.setToken(null);
				userService.save(user);
				
				redirectAttributes.addFlashAttribute("success", "Tạo tài khoản thành công, vui lòng đăng nhập!");
				return "redirect:/login";
			}else {
				redirectAttributes.addFlashAttribute("danger", "Sai mã token!");
				return "redirect:/register/confirm-token?" + userId;
			}
		}catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("danger", "Có lỗi khi xác nhận tài khoản!");
			return "redirect:/register";
		}
		
		
	}
	
	
}
