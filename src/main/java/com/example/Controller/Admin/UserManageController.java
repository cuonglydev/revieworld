package com.example.Controller.Admin;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.User;
import com.example.Entity.DefaultRank;
import com.example.Entity.Deposit;
import com.example.Entity.Withdraw;
import com.example.Entity.Mission;
import com.example.Entity.Rank;
import com.example.Entity.UserAffiliate;
//import com.example.Entity.Dispute;
import com.example.Service.UserService;
import com.example.Service.DefaultRankService;
import com.example.Service.DepositService;
import com.example.Service.WithdrawService;
import com.example.Service.MissionService;
import com.example.Service.RankService;
import com.example.Service.UserAffiliateService;
//import com.example.Service.DisputeService;


@Controller
@RequestMapping("/admin")
public class UserManageController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private DepositService depositService;
	
	@Autowired
	private WithdrawService withdrawService;
	
	@Autowired
	private MissionService missionService;
	
	@Autowired
	private UserAffiliateService userAffiliateService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private RankService rankService;
	
	@Autowired
	DefaultRankService defaultRankService;


	@GetMapping("/user")
	public String userPage(Model model) {
		List<User> users = userService.findAll();
		model.addAttribute("users", users);
		List<Rank> ranks = rankService.getAllRanks();
		model.addAttribute("ranks", ranks);
		DefaultRank defaultRank = defaultRankService.findById(1);
		model.addAttribute("defaultRank", defaultRank);
		return "Admin/Pages/User/user";
	}
	
	
	@GetMapping("/user/{id}")
	public String userDetailPage(@PathVariable int id, Model model) {
		try {
			User user = userService.findById(id);
			model.addAttribute("user", user);
			List<Rank> ranks = rankService.getAllRanks();
			model.addAttribute("ranks", ranks);
		}catch (Exception e) {
			model.addAttribute("danger", "Lỗi không tìm thấy người dùng!");
		}
		return "Admin/Pages/User/user-detail";
	}

	
	@GetMapping("/user/detail/{id}")
	public String getUserDetail(@PathVariable("id") int userId, Model model) {
		try {
			User user = userService.findById(userId);
			if (user != null) {
				List<Deposit> deposits;
				try {
					deposits = Optional.ofNullable(depositService.findByUserId(userId)).orElse(new ArrayList<>()).stream().filter(d -> d != null).toList();
				} catch (Exception e) {
					deposits = new ArrayList<>();
				}
				model.addAttribute("deposits", deposits);
				
				List<Withdraw> withdraws;
				try {
					withdraws = Optional.ofNullable(withdrawService.findByUserId(userId)).orElse(new ArrayList<>()).stream().filter(w -> w != null).toList();
				} catch (Exception e) {
					withdraws = new ArrayList<>();
				}
				model.addAttribute("witdraws", withdraws);
				

// 				List<Mission> missions;
// 				try {
// 					missions = Optional.ofNullable(missionService.findByUserId(userId)).orElse(new ArrayList<>()).stream().filter(m -> m != null).toList();
// 				} catch (Exception e) {
// 					missions = new ArrayList<>();
// 				}

				List<Mission> missions = missionService.findAllByUserId(userId);

				model.addAttribute("missions", missions);
				
				List<User> referrals = new ArrayList<>();
				try {
					List<UserAffiliate> affiliates = Optional.ofNullable(userAffiliateService.findAllByUserId(userId)).orElse(new ArrayList<>());
					for (UserAffiliate affiliate : affiliates) {
						if (affiliate != null && affiliate.getUser() != null) {
							referrals.add(affiliate.getUser());
						}
					}
				} catch (Exception e) {
					// referrals đã là list rỗng
				}
				model.addAttribute("referrals", referrals);
				
				double totalDeposited = 0.0;
				try {
					totalDeposited = deposits.stream()
							.filter(d -> d.getStatus() != null && d.getStatus().equals("SUCCESS"))
							.mapToDouble(d -> d.getAmount() != null ? d.getAmount() : 0.0)
							.sum();
					user.setTotalDeposited(totalDeposited);
				} catch (Exception e) {
					// Bỏ qua nếu lỗi
				}
				
				model.addAttribute("user", user);
				return "Admin/Pages/User/Fragments/user-modals :: userDetailModal";
			}
			model.addAttribute("error", "Không tìm thấy người dùng hoặc dữ liệu liên quan!");
			return "Admin/Pages/User/Fragments/user-modals :: userDetailModal";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "Có lỗi xảy ra khi tải thông tin chi tiết: " + e.getMessage());
			return "Admin/Pages/User/Fragments/user-modals :: userDetailModal";
		}
	}

	@PostMapping("/user/save")
	public String saveUser(
			@RequestParam String email,
			@RequestParam String username,
			@RequestParam String password,
			/* @RequestParam User.Rank rank, */
			@RequestParam String status,
			RedirectAttributes redirectAttributes) {
		try {
			User existingUser = userService.findByEmail(email);
			if (existingUser != null) {
				redirectAttributes.addFlashAttribute("danger", "Email đã tồn tại trong hệ thống!");
				return "redirect:/admin/user";
			}

			User user = new User();
			user.setEmail(email);
			user.setUsername(username);
			user.setPassword(passwordEncoder.encode(password));
			/* user.setRank(rank); */
			user.setStatus(status);
			user.setCreatedAt(new Date());
			user.setAmount(0.0);
			user.setBonusAmount(0.0);
			user.setTotalDeposited(0.0);
			
			userService.save(user);
			redirectAttributes.addFlashAttribute("success", "Thêm người dùng thành công!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Có lỗi xảy ra: " + e.getMessage());
		}
		return "redirect:/admin/user";
	}

	@PostMapping("/user/update-status")
	@ResponseBody
	public String updateUserStatus(@RequestParam("userId") int userId, @RequestParam("status") String status) {
		try {
			User user = userService.findById(userId);
			if (user == null) {
				return "Không tìm thấy người dùng!";
			}
			
			user.setStatus(status);
			userService.save(user);
			
			return "Cập nhật trạng thái thành công!";
		} catch (Exception e) {
			return "Có lỗi xảy ra: " + e.getMessage();
		}
	}

	@PostMapping("/user/delete/{id}")
	@ResponseBody
	public ResponseEntity<?> deleteUser(@PathVariable("id") int userId) {
		try {
			User user = userService.findById(userId);
			if (user == null) {
				return ResponseEntity.notFound().build();
			}
			
			// Delete all related deposits
			List<Deposit> deposits = depositService.findByUserId(userId);
			for (Deposit deposit : deposits) {
				depositService.delete(deposit.getId());
			}
			
			// Delete all related withdraws
			List<Withdraw> withdraws = withdrawService.findByUserId(userId);
			for (Withdraw withdraw : withdraws) {
				withdrawService.delete(withdraw.getId());
			}
			
			// Delete all related missions
			List<Mission> missions = missionService.findAllByUserId(userId);
			for (Mission mission : missions) {
				missionService.delete(mission.getId());
			}
			
			// Delete all related disputes
//			List<Dispute> disputes = disputeService.findByUserId(userId);
//			for (Dispute dispute : disputes) {
//				disputeService.delete(dispute.getId());
//			}
			
			// Delete all affiliate relationships
			List<UserAffiliate> affiliates = userAffiliateService.findAllByUserId(userId);
			for (UserAffiliate affiliate : affiliates) {
				userAffiliateService.delete(affiliate.getId());
			}
			
			// Finally delete the user
			userService.deleteById(userId);
			return ResponseEntity.ok().body("Xóa người dùng thành công!");
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Có lỗi xảy ra khi xóa người dùng: " + e.getMessage());
		}
	}

	@PostMapping("/update/{id}")
	@ResponseBody
	public String updateUser(@PathVariable("id") int id,
			@RequestParam("email") String email,
			@RequestParam("username") String username,
			@RequestParam(value = "newPassword", required = false) String newPassword,
			/* @RequestParam("rank") User.Rank rank, */
			@RequestParam("status") String status) {
		try {
			User user = userService.findById(id);
			if (user == null) {
				return "Không tìm thấy người dùng";
			}

			User existingUser = userService.findByEmail(email);
			if (existingUser != null && existingUser.getId() != id) {
				return "Email đã được sử dụng bởi người dùng khác";
			}

			existingUser = userService.findByUsername(username);
			if (existingUser != null && existingUser.getId() != id) {
				return "Tên đăng nhập đã được sử dụng bởi người dùng khác";
			}

			user.setEmail(email);
			user.setUsername(username);
			
			if (newPassword != null && !newPassword.trim().isEmpty()) {
				user.setPassword(passwordEncoder.encode(newPassword));
			}
			
//			user.setRank(rank);
			user.setStatus(status);
			userService.save(user);
			
			return "Cập nhật thông tin thành công";
		} catch (Exception e) {
			e.printStackTrace();
			return "Có lỗi xảy ra: " + e.getMessage();
		}
	}

//	@PostMapping("/dispute/approve/{id}")
//	@ResponseBody
//	public String approveDispute(@PathVariable("id") int disputeId) {
//		try {
//			Dispute dispute = disputeService.findById(disputeId);
//			if (dispute == null) {
//				return "Không tìm thấy kháng đơn";
//			}
//			
//			if (!dispute.getStatus().equals("PENDING")) {
//				return "Kháng đơn này đã được xử lý";
//			}
//			
//			dispute.setStatus("APPROVED");
//			dispute.setUpdatedAt(new Date());
//			disputeService.save(dispute);
//			
//			Mission mission = dispute.getMission();
//			mission.setStatus("COMPLETED");
//			missionService.save(mission);
//			
//			return "Duyệt kháng đơn thành công";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "Có lỗi xảy ra: " + e.getMessage();
//		}
//	}
//
//	@PostMapping("/dispute/reject/{id}")
//	@ResponseBody
//	public String rejectDispute(@PathVariable("id") int disputeId, @RequestParam String reason) {
//		try {
//			Dispute dispute = disputeService.findById(disputeId);
//			if (dispute == null) {
//				return "Không tìm thấy kháng đơn";
//			}
//			
//			if (!dispute.getStatus().equals("PENDING")) {
//				return "Kháng đơn này đã được xử lý";
//			}
//			
//			dispute.setStatus("REJECTED");
//			dispute.setRejectReason(reason);
//			dispute.setUpdatedAt(new Date());
//			disputeService.save(dispute);
//			
//			Mission mission = dispute.getMission();
//			mission.setStatus("FAILED");
//			missionService.save(mission);
//			
//			return "Từ chối kháng đơn thành công";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "Có lỗi xảy ra: " + e.getMessage();
//		}
//	}

	// @GetMapping("/rank")
	// public String rankPage() {
	// return "Admin/Pages/User/rank";
	// }


}
