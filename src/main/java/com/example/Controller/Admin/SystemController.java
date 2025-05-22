package com.example.Controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Entity.SystemSetting;
import com.example.Service.SystemService;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/system")
@PreAuthorize("hasRole('ADMIN')")
public class SystemController {

    @Autowired
    private SystemService systemService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('SYSTEM_VIEW')")
    public String systemPage(Model model) {
        SystemSetting settings = systemService.getLatestSettings();
        if (settings == null) {
            settings = new SystemSetting();
            settings.setMinDepositAmount(systemService.getMinDepositAmount());
            settings.setMaxDepositAmount(systemService.getMaxDepositAmount());
            settings.setUsdToVndRate(systemService.getUsdToVndRate());
            settings.setCommissionRate(systemService.getCommissionRate());
            settings.setAffiliateCommissionPercentage(systemService.getAffiliateCommissionRate());
            settings.setReferralCommissionRate(systemService.getReferralCommissionRate());
            settings.setCurrencyExchangeRate(systemService.getCurrencyExchangeRate());
        }
        model.addAttribute("settings", settings);
        return "Admin/Pages/System/system";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('SYSTEM_EDIT')")
    public ResponseEntity<?> saveSettings(@RequestBody SystemSetting settings) {
        try {
            systemService.validateSettings(settings);
            systemService.saveSettings(settings);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cài đặt đã được lưu thành công!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/deposit-limits")
    @PreAuthorize("hasAuthority('SYSTEM_EDIT')")
    public ResponseEntity<?> updateDepositLimits(@RequestParam Double minAmount, @RequestParam Double maxAmount) {
        try {
            systemService.updateDepositLimits(minAmount, maxAmount);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/commission-rates")
    @PreAuthorize("hasAuthority('SYSTEM_EDIT')")
    public ResponseEntity<?> updateCommissionRates(
            @RequestParam Double commissionRate,
            @RequestParam Double newUserRate,
            @RequestParam Double affiliateRate,
            @RequestParam Double referralRate) {
        try {
            systemService.updateCommissionRates(commissionRate, newUserRate, affiliateRate, referralRate);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/currency-rates")
    @PreAuthorize("hasAuthority('SYSTEM_EDIT')")
    public ResponseEntity<?> updateCurrencyRates(
            @RequestParam Double usdToVndRate,
            @RequestParam Double exchangeRate) {
        try {
            systemService.updateCurrencyRates(usdToVndRate, exchangeRate);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/support-info")
    @PreAuthorize("hasAuthority('SYSTEM_EDIT')")
    public ResponseEntity<?> updateSupportInfo(
            @RequestParam String telegramLink,
            @RequestParam String email) {
        try {
            systemService.updateSupportInfo(telegramLink, email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 