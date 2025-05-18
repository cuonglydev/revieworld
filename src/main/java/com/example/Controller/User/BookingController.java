package com.example.Controller.User;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Common.Slugify;
import com.example.Common.TokenGenerator;
import com.example.Entity.DefaultRank;
import com.example.Entity.Language;
import com.example.Entity.Order;
import com.example.Entity.OrderType;
import com.example.Entity.User;
import com.example.Service.DefaultRankService;
import com.example.Service.LanguageService;
import com.example.Service.OrderService;
import com.example.Service.OrderTypeService;
import com.example.Service.UploadService;
import com.example.Service.UserService;

@Controller
public class BookingController {

	@Autowired
	OrderTypeService orderTypeService;

	@Autowired
	UserService userService;

	@Autowired
	LanguageService languageService;
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private Slugify slugify;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	
	@Value("${upload.dir}")
	private String uploadDir;
	
	 @Value("${static-folder}")
	 private String staticFolder;
	 

	@GetMapping("/booking")
	public String bookingPage(Model model) {
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);

		List<OrderType> orderTypes = orderTypeService.getAllOrderTypes();
		model.addAttribute("orderTypes", orderTypes);
		
		List<Order> orders = orderService.findAllByUserAndStatus(user.getId(), "ACTIVE");
		model.addAttribute("orders", orders);
		return "User/Pages/Booking/booking";
	}

	@GetMapping("/booking/{slug}")
	public String bookingDetailPage(@PathVariable String slug, Model model) {
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);
		
		List<Language> languages = languageService.findAll();
		model.addAttribute("languages", languages);
		
		OrderType orderType = orderTypeService.findBySlug(slug);
		model.addAttribute("orderType", orderType);
		return "User/Pages/Booking/booking-detail";
	}

	@PostMapping("/booking/create")
	public String createBooking(@ModelAttribute Order order, @RequestParam(value = "file") MultipartFile photo,
			@RequestParam("languageId") int languageId, @RequestParam("orderTypeId") int orderTypeId, RedirectAttributes redirectAttributes) {
		User user = userService.getCurrentUser();
		Date currentDate = new Date();
		
		OrderType orderType = orderTypeService.findById(orderTypeId);
		Language language = languageService.findById(languageId);
		
		String slugToken = tokenGenerator.generateToken();
		
		
		try {
			
			
			Double price = (language.getPromotionalPrice() != null)
					? language.getPromotionalPrice()
					: language.getPrice();
			
			if(user.getRank() != null) {
				Double percentagePrice = (price / 100) * user.getRank().getPercentage();
				price -= percentagePrice;
			}
			
			Double totalAmount = price * order.getQuantity();
			
			if(user.getAmount() < totalAmount) {
				redirectAttributes.addFlashAttribute("danger", "Số dư ví bạn không đủ!");
				return "redirect:/booking/" + orderType.getSlug();
			}
			
			order.setTotalAmount(totalAmount);
			
			order.setItemAmount(price);
			
			
			if(!photo.isEmpty()) {
				String urlFileName = uploadService.saveFile(photo, "images");
				order.setPhoto(staticFolder + "images/" + urlFileName);
			}else {
				order.setPhoto(null);
			}
			order.setCreatedAt(currentDate);
			
			order.setTurnQuantityDone(0);
			order.setQuantityDone(0);
			
			String slug = slugify.toSlug(order.getName());
			order.setSlug(slug + "-" + slugToken);
			
			Order existingOrder = orderService.findBySlug(slug);
			if(existingOrder != null) {
				redirectAttributes.addFlashAttribute("danger", "Lỗi hệ thống, vui lòng tạo lại đơn mới!");
				return "redirect:/booking";
			}
			order.setStatus("ACTIVE");

			order.setLanguage(language.getName());
			
			order.setUser(user);
			order.setOrderType(orderType);
			orderService.save(order);
			
			user.setAmount(user.getAmount() - totalAmount);
			userService.save(user);
			
			redirectAttributes.addFlashAttribute("success", "Tạo đơn thành công!");
			return "redirect:/order	/" + orderType.getSlug() + "/" + order.getSlug();
		}catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("danger", "Tạo đơn thất bại!");
			return "redirect:/booking/" + orderType.getSlug();
		}
		
		
	}
	
}
