package com.example.Controller.Admin;

import com.example.Entity.Affiliate;
import com.example.Entity.DefaultWithdraw;
import com.example.Entity.Rate;
import com.example.Service.AffiliateService;
import com.example.Service.RateService;
import com.example.Service.defaultwithdrawService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class SettingController {

    @Autowired
    private defaultwithdrawService defaultWithdrawService;

    @Autowired
    private AffiliateService affiliateService;

    @Autowired
    private RateService rateService;

    @GetMapping("/setting")
    public String getSetting(Model model) {
        model.addAttribute("rate", rateService.findByCountry("vi"));
        model.addAttribute("affiliate", affiliateService.findByid(1));
        model.addAttribute("withdraw", defaultWithdrawService.findById(1));
        return "Admin/Pages/Setting/Affiliate";
    }

    @PostMapping("/Affiliate/edit")
    public String updateAffiliate(@ModelAttribute Affiliate affiliate, RedirectAttributes ra) {
        if (!isValidPercentage(affiliate.getPercentage())) {
            ra.addFlashAttribute("danger", "Tỷ lệ hoa hồng phải từ 0 đến 100");
            return "redirect:/admin/setting";
        }

        try {
            Affiliate existing = affiliateService.findByid(1);
            existing.setName(affiliate.getName());
            existing.setPercentage(affiliate.getPercentage());
            existing.setStatus(affiliate.getStatus());
            affiliateService.save(existing);
            ra.addFlashAttribute("success", "Cập nhật thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("danger", "Cập nhật thất bại");
        }

        return "redirect:/admin/setting";
    }

    @PostMapping("/Rate/edit")
    public String updateRate(@ModelAttribute Rate rate, RedirectAttributes ra) {
        if (!isValidPercentage(rate.getRate())) {
            ra.addFlashAttribute("danger", "Tỷ lệ phải từ 0 đến 100");
            return "redirect:/admin/setting";
        }

        try {
            Rate existing = rateService.findByCountry("vi");
            existing.setRate(rate.getRate());
            existing.setCountry(rate.getCountry());
            rateService.save(existing);
            ra.addFlashAttribute("success", "Cập nhật thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("danger", "Cập nhật thất bại");
        }

        return "redirect:/admin/setting";
    }

    @PostMapping("/withdraw/edit")
    public String updateWithdraw(@ModelAttribute DefaultWithdraw input, RedirectAttributes ra) {
        try {
            DefaultWithdraw existing = defaultWithdrawService.findById(1);
            if (existing == null) {
                ra.addFlashAttribute("danger", "Không tìm thấy bản ghi để cập nhật");
                return "redirect:/admin/setting";
            }

            existing.setMinAmount(input.getMinAmount());
            existing.setMaxAmount(input.getMaxAmount());
            defaultWithdrawService.save(existing);
            ra.addFlashAttribute("success", "Cập nhật thành công");
        } catch (Exception e) {
            ra.addFlashAttribute("danger", "Cập nhật thất bại");
        }

        return "redirect:/admin/setting";
    }

    private boolean isValidPercentage(double percent) {
        return percent >= 0 && percent <= 100;
    }
}
