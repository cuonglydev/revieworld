package com.example.Controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Entity.Affiliate;
import com.example.Service.AffiliateService;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/affiliate")
public class AffiliateController {

    @Autowired
    private AffiliateService affiliateService;

    @GetMapping("")
    public String affiliatePage(Model model) {
        Affiliate affiliate = affiliateService.findByid(1);
        if (affiliate == null) {
            affiliate = new Affiliate();
        }
        model.addAttribute("affiliate", affiliate);
        return "Admin/Pages/Affiliate/affiliate";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<?> saveSettings(@RequestBody Affiliate affiliate) {
        Map<String, Object> response = new HashMap<>();
        try {
            Affiliate savedAffiliate = affiliateService.saveAffiliate(affiliate);
            response.put("success", true);
            response.put("message", "Cài đặt hoa hồng đã được lưu thành công!");
            response.put("data", savedAffiliate);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra khi lưu cài đặt: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/status")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@RequestParam("status") Boolean status) {
        Map<String, Object> response = new HashMap<>();
        try {
            Affiliate affiliate = affiliateService.findByid(1);
            if (affiliate == null) {
                affiliate = new Affiliate();
            }
            affiliate.setStatus(status);
            affiliateService.saveAffiliate(affiliate);
            
            response.put("success", true);
            response.put("message", "Trạng thái chương trình đã được cập nhật!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/stats")
    @ResponseBody
    public ResponseEntity<?> getAffiliateStats() {
        Map<String, Object> response = new HashMap<>();
        try {
            Affiliate affiliate = affiliateService.findByid(1);
            if (affiliate != null) {
                Map<String, Object> stats = new HashMap<>();
                stats.put("isActive", affiliate.getStatus());
                stats.put("baseCommission", affiliate.getBaseCommissionPercentage());
                stats.put("newUserCommission", affiliate.getNewUserCommissionPercentage());
                stats.put("referralCommission", affiliate.getReferralCommissionPercentage());
                stats.put("minAmount", affiliate.getMinCommissionAmount());
                stats.put("maxAmount", affiliate.getMaxCommissionAmount());
                
                response.put("success", true);
                response.put("data", stats);
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy cài đặt hoa hồng");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 