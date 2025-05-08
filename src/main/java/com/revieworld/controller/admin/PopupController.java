package com.revieworld.controller.admin;

import com.revieworld.model.Popup;
import com.revieworld.service.PopupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/admin/popups")
public class PopupController {

    @Autowired
    private PopupService popupService;

    @PostMapping("/{id}/update")
    public ResponseEntity<?> updatePopup(@PathVariable Long id, 
                                       @RequestBody Map<String, Object> updates,
                                       Principal principal) {
        try {
            Popup popup = popupService.getPopupById(id);
            if (popup == null) {
                return ResponseEntity.notFound().build();
            }

            // Cập nhật các trường dựa trên dữ liệu gửi lên
            if (updates.containsKey("active")) {
                popup.setActive((Boolean) updates.get("active"));
            }
            if (updates.containsKey("position")) {
                popup.setPosition((String) updates.get("position"));
            }
            if (updates.containsKey("duration")) {
                popup.setDuration((Integer) updates.get("duration"));
            }
            if (updates.containsKey("animation")) {
                popup.setAnimation((String) updates.get("animation"));
            }
            if (updates.containsKey("title")) {
                popup.setTitle((String) updates.get("title"));
            }
            if (updates.containsKey("content")) {
                popup.setContent((String) updates.get("content"));
            }

            // Cập nhật thông tin người sửa
            popup.setUpdatedBy(principal.getName());
            
            popupService.savePopup(popup);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cập nhật thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPopup(@RequestBody Popup popup, Principal principal) {
        try {
            popup.setCreatedBy(principal.getName());
            popup.setUpdatedBy(principal.getName());
            popupService.savePopup(popup);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Thêm mới thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deletePopup(@PathVariable Long id) {
        try {
            popupService.deletePopup(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Xóa thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 