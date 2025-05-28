package com.example.Controller.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.example.Entity.OrderContent;
import com.example.Entity.OrderPhoto;
import com.example.Entity.OrderType;
import com.example.Entity.User;
import com.example.Service.DefaultRankService;
import com.example.Service.LanguageService;
import com.example.Service.OrderContentService;
import com.example.Service.OrderPhotoService;
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

	@Autowired
	private OrderPhotoService orderPhotoService;

	@Autowired
	private OrderContentService orderContentService;

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

		List<String> statuses = Arrays.asList("ACTIVE", "PAUSE");
		List<Order> orders = orderService.findAllByUserIdAndStatusIn(user.getId(), statuses);
		model.addAttribute("orders", orders);
		return "User/Pages/Booking/booking";
	}

	@GetMapping("/booking/{slug}")
	public String bookingDetailPage(@PathVariable String slug, Model model) {
		User user = userService.getCurrentUser();
		model.addAttribute("user", user);
		OrderType orderType = orderTypeService.findBySlug(slug);
		model.addAttribute("orderType", orderType);

		List<Language> languages = languageService.findAllByOrderTypeId(orderType.getId());
		model.addAttribute("languages", languages);

		return "User/Pages/Booking/booking-detail";
	}

	@PostMapping("/booking/create")
	public String createBooking(@ModelAttribute Order order, @RequestParam(value = "file") MultipartFile photo,
			@RequestParam(value = "content", required = false) MultipartFile content,
			@RequestParam(value = "photos", required = false) List<MultipartFile> photos,
			@RequestParam("languageId") int languageId, @RequestParam("orderTypeId") int orderTypeId,
			RedirectAttributes redirectAttributes) {
		User user = userService.getCurrentUser();
		Date currentDate = new Date();

		OrderType orderType = orderTypeService.findById(orderTypeId);
		Language language = languageService.findById(languageId);

		String slugToken = tokenGenerator.generateToken();

		try {
			int numberPhotos = photos.size();
			if (numberPhotos > order.getQuantity()) {
				redirectAttributes.addFlashAttribute("danger", "Số lượng ảnh quá lớn!");
				return "redirect:/booking/" + orderType.getSlug();
			}

			Double price = (language.getPromotionalPrice() != null) ? language.getPromotionalPrice()
					: language.getPrice();

			if (user.getRank() != null) {
				Double percentagePrice = (price / 100) * user.getRank().getPercentage();
				price -= percentagePrice;
			}

			Double totalAmount = price * order.getQuantity();

			if (user.getAmount() < totalAmount) {
				redirectAttributes.addFlashAttribute("danger", "Số dư ví bạn không đủ!");
				return "redirect:/booking/" + orderType.getSlug();
			}

			order.setTotalAmount(totalAmount);

			order.setItemAmount(price);

			if (!photo.isEmpty()) {
				String urlFileName = uploadService.saveFile(photo, "images");
				order.setPhoto(staticFolder + "images/" + urlFileName);
			} else {
				order.setPhoto(null);
			}
			order.setCreatedAt(currentDate);

			order.setTurnQuantityDone(0);
			order.setQuantityDone(0);

			String slug = slugify.toSlug(order.getName());
			String fullSlug = slug + "-" + slugToken;
			order.setSlug(fullSlug);

			Order existingOrder = orderService.findBySlug(fullSlug);
			if (existingOrder != null) {
				redirectAttributes.addFlashAttribute("danger", "Lỗi hệ thống, vui lòng tạo lại đơn mới!");
				return "redirect:/booking";
			}
			order.setStatus("ACTIVE");

			order.setLanguage(language.getName());

			order.setOrderTypeName(orderType.getName());
			order.setOrderTypeLink(orderType.getLink());
			order.setOrderTypeUrl(orderType.getUrl());

			order.setUser(user);
			order.setOrderType(orderType);
			orderService.save(order);

			if (photos != null) {
				for (MultipartFile file : photos) {
					if (file != null && !file.isEmpty()) {

						OrderPhoto orderPhoto = new OrderPhoto();
						String urlFileName = uploadService.saveFile(file, "images");
						orderPhoto.setPhoto(staticFolder + "images/" + urlFileName);
						orderPhoto.setOrder(order);
						orderPhoto.setStatus("ACTIVE");
						orderPhotoService.save(orderPhoto);
					}
				}
			}

			if (content != null && !content.isEmpty()) {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content.getInputStream(), StandardCharsets.UTF_8));
				String line;

				while ((line = reader.readLine()) != null) {
					OrderContent orderContent = new OrderContent();
					orderContent.setContent(line);
					orderContent.setStatus("ACTIVE");
					orderContent.setOrder(order);
					orderContentService.save(orderContent);
				}
			}

			user.setAmount(user.getAmount() - totalAmount);
			userService.save(user);

			redirectAttributes.addFlashAttribute("success", "Tạo đơn thành công!");
			return "redirect:/order/" + order.getSlug();
		} catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("danger", "Tạo đơn thất bại!");
			return "redirect:/booking/" + orderType.getSlug();
		}

	}

}
