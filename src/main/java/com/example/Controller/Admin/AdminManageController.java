package com.example.Controller.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Admin;
import com.example.Security.CustomAdminDetails;
import com.example.Security.SecurityUtils;
import com.example.Service.AdminService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminManageController {

	@Autowired
	AdminService adminService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/admin")
	public String adminPage(Model model) {
		List<Admin> admins = adminService.findAll();
		model.addAttribute("admins", admins);
		return "Admin/Pages/Admin/admin";
	}

	@PostMapping("/admin/save")
	public String saveAdmin(@ModelAttribute Admin admin,
			@RequestParam(value = "password", required = false) String password,
			RedirectAttributes redirectAttributes) {

		try {
			Admin existingAdmin = adminService.findByUsername(admin.getUsername());
			if (existingAdmin != null && existingAdmin.getId() != admin.getId()) {
				redirectAttributes.addFlashAttribute("danger", "Tên tài khoản này đã được sử dụng!");
				return "redirect:/admin/admin";
			}

			Admin getAdmin = adminService.findById(admin.getId());

			if (getAdmin != null) {

				if (password == null || password.isEmpty()) {
					admin.setPassword(getAdmin.getPassword());
				} else {

					String encodedPassword = passwordEncoder.encode(password);
					admin.setPassword(encodedPassword);
				}

			} else {
				if (password != null && !password.isEmpty()) {
					String encodedPassword = passwordEncoder.encode(password);
					admin.setPassword(encodedPassword);
				}
			}

			adminService.save(admin);
			redirectAttributes.addFlashAttribute("success", "Lưu thành công!");
			return "redirect:/admin/admin";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Lưu thất bại!");
			return "redirect:/admin/admin";
		}
	}

	@GetMapping("/admin/delete/{id}")
	public String deleteById(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) {

		Admin admin = SecurityUtils.getCurrentAdmin();
		try {

			if (admin.getId() == id) {
				redirectAttributes.addFlashAttribute("danger", "Không thể xóa tài khoản đang đăng nhập!");
				return "redirect:/admin/admin";
			}

			adminService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
			return "redirect:/admin/admin";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa thất bại!");
			return "redirect:/admin/admin";
		}
	}
}
