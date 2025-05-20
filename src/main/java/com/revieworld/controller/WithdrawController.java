package com.revieworld.controller;

import com.example.Entity.Withdraw;
import com.example.Entity.User;
import com.example.Entity.UserBank;
import com.revieworld.service.WithdrawService;
import com.revieworld.service.UserBankService;
import com.revieworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/account")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;
    
    @Autowired
    private UserBankService userBankService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/withdraw")
    public String showWithdrawPage(@AuthenticationPrincipal User user, Model model) {
        List<UserBank> userBanks = userBankService.getUserBanks(user.getId());
        model.addAttribute("userBanks", userBanks);
        return "User/Pages/Account/withdraw";
    }

    @PostMapping("/withdraw")
    public String processWithdraw(
            @AuthenticationPrincipal User user,
            @RequestParam("amount") Double amount,
            @RequestParam("bankId") Long bankId,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Validate amount
            if (amount < 5 || amount > 500) {
                redirectAttributes.addFlashAttribute("error", "Số tiền rút phải từ $5 đến $500");
                return "redirect:/account/withdraw";
            }

            // Get bank info
            UserBank userBank = userBankService.getUserBankById(bankId);
            if (userBank == null || !userBank.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Tài khoản ngân hàng không hợp lệ");
                return "redirect:/account/withdraw";
            }

            // Create withdraw request
            Withdraw withdraw = new Withdraw();
            withdraw.setAmount(amount);
            withdraw.setUser(user);
            withdraw.setStatus("PENDING");
            withdraw.setUserBankMethod(userBank.getMethod());
            withdraw.setBankName(userBank.getBankName());
            withdraw.setAccountName(userBank.getAccountName());
            withdraw.setAccountNumber(userBank.getAccountNumber());
            withdraw.setCreateAt(new Date());

            // Process withdrawal
            withdrawService.processWithdrawal(withdraw);

            redirectAttributes.addFlashAttribute("success", "Yêu cầu rút tiền đã được gửi thành công");
            return "redirect:/account/withdraw";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/account/withdraw";
        }
    }
} 