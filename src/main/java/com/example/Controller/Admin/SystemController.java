package com.example.Controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/*import com.example.Entity.SystemSetting;
import com.example.Service.SystemSettingService;*/
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/system")
public class SystemController {

	/*
	 * @Autowired private SystemSettingService systemSettingService;
	 * 
	 * @GetMapping("") public String systemPage(Model model) { // Load system
	 * settings SystemSetting settings = systemSettingService.getLatestSettings();
	 * if (settings == null) { settings = new SystemSetting();
	 * settings.setMinDepositAmount(5.0); settings.setMaxDepositAmount(10000.0);
	 * settings.setUsdToVndRate(23000.0); settings.setCommissionRate(5.0);
	 * settings.setAffiliateCommissionPercentage(5.0);
	 * settings.setReferralCommissionRate(5.0);
	 * settings.setCurrencyExchangeRate(23000.0); } model.addAttribute("settings",
	 * settings);
	 * 
	 * // Add any other model attributes needed for the system page
	 * 
	 * return "Admin/Pages/System/system"; }
	 * 
	 * @PostMapping("/save") public ResponseEntity<?> saveSettings(@RequestBody
	 * SystemSetting settings) { try { // Validate settings if
	 * (settings.getMinDepositAmount() < 0 || settings.getMaxDepositAmount() < 0) {
	 * throw new IllegalArgumentException("Số tiền nạp không thể âm"); } if
	 * (settings.getMinDepositAmount() > settings.getMaxDepositAmount()) { throw new
	 * IllegalArgumentException("Số tiền nạp tối thiểu không thể lớn hơn số tiền nạp tối đa"
	 * ); } if (settings.getUsdToVndRate() <= 0) { throw new
	 * IllegalArgumentException("Tỉ giá USD/VND phải lớn hơn 0"); } if
	 * (settings.getCommissionRate() < 0 || settings.getCommissionRate() > 100) {
	 * throw new IllegalArgumentException("Tỉ lệ hoa hồng phải từ 0-100%"); } if
	 * (settings.getAffiliateCommissionPercentage() < 0 ||
	 * settings.getAffiliateCommissionPercentage() > 100) { throw new
	 * IllegalArgumentException("Tỉ lệ hoa hồng giới thiệu phải từ 0-100%"); }
	 * 
	 * systemSettingService.saveSettings(settings);
	 * 
	 * Map<String, Object> response = new HashMap<>(); response.put("success",
	 * true); response.put("message", "Cài đặt đã được lưu thành công!"); return
	 * ResponseEntity.ok(response);
	 * 
	 * } catch (Exception e) { Map<String, Object> response = new HashMap<>();
	 * response.put("success", false); response.put("message", "Có lỗi xảy ra: " +
	 * e.getMessage()); return ResponseEntity.badRequest().body(response); } }
	 * 
	 * // Add other system-related endpoints here
	 */} 