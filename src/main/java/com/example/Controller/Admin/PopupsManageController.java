package com.example.Controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Repository.PopupsRepository;
import com.example.Entity.Popups;
import com.example.Service.PopupsService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/admin")
public class PopupsManageController {

	@Autowired
	private PopupsRepository popupsRepository;

	@Autowired
	private PopupsService popupsService;

	@GetMapping("/popups")
	public String popupsPage(Model model) {
		model.addAttribute("popups", popupsRepository.findAll());
		
		Popups popup = popupsService.findById(1);
		model.addAttribute("popup", popup);
		
		return "Admin/Pages/Popups/popups";
	}
	
	@PostMapping("/popups/add")
	@ResponseBody
	public Map<String, Object> addPopup(@RequestBody Map<String, Object> data) {
		Map<String, Object> result = new HashMap<>();
		try {
			Popups popup = new Popups();
			popup.setTitle((String) data.get("title"));
			popup.setContent((String) data.get("content"));
			popup.setStatus((Boolean) data.get("active"));
			popup.setName((String) data.getOrDefault("name", ""));
			popupsService.createSinglePopup(popup);
			result.put("success", true);
			result.put("message", "Tạo thông báo thành công!");
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "Tạo thông báo thất bại!");
		}
		return result;
	}

	// API lấy chi tiết popup
	@GetMapping("/popups/{id}")
	@ResponseBody
	public Map<String, Object> getPopup(@PathVariable("id") int id) {
		Map<String, Object> result = new HashMap<>();
		Popups popup = popupsRepository.findById(id).orElse(null);
		if (popup != null) {
			result.put("id", popup.getId());
			result.put("title", popup.getTitle());
			result.put("content", popup.getContent());
			result.put("active", popup.getStatus());
			result.put("name", popup.getName());
			result.put("success", true);
		} else {
			result.put("success", false);
			result.put("message", "Không tìm thấy thông báo");
		}
		return result;
	}

	// API cập nhật popup
	@PostMapping("/popups/{id}/update")
	@ResponseBody
	public Map<String, Object> updatePopup(@PathVariable("id") int id, @RequestBody Map<String, Object> data) {
		Map<String, Object> result = new HashMap<>();
		try {
			Popups popup = popupsRepository.findById(id).orElse(null);
			if (popup == null) {
				result.put("success", false);
				result.put("message", "Không tìm thấy thông báo");
				return result;
			}
			if (data.containsKey("title")) popup.setTitle((String) data.get("title"));
			if (data.containsKey("content")) popup.setContent((String) data.get("content"));
			if (data.containsKey("active")) popup.setStatus((Boolean) data.get("active"));
			if (data.containsKey("name")) popup.setName((String) data.get("name"));
			popupsRepository.save(popup);
			result.put("success", true);
			result.put("message", "Cập nhật thành công!");
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "Cập nhật thất bại!");
		}
		return result;
	}

	// API xóa popup
	@PostMapping("/popups/delete/{id}")
	@ResponseBody
	public Map<String, Object> deletePopup(@PathVariable("id") int id) {
		Map<String, Object> result = new HashMap<>();
		try {
			popupsRepository.deleteById(id);
			result.put("success", true);
			result.put("message", "Xóa thành công!");
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "Xóa thất bại!");
		}
		return result;
	}
}
