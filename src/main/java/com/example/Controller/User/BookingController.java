package com.example.Controller.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookingController {

	@GetMapping("/booking")
	public String bookingPage() {
		return "User/Pages/Booking/booking";
	}

	@GetMapping("/booking-detail")
	public String bookingDetailPage() {
		return "User/Pages/Booking/booking-detail";
	}

}
