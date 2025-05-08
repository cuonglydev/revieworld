package com.revieworld.controller;

import com.revieworld.model.WithdrawalRequest;
import com.revieworld.model.WithdrawalRequest.WithdrawalStatus;
import com.revieworld.service.WithdrawalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/withdrawals")
public class AdminWithdrawalController {

    @Autowired
    private WithdrawalRequestService withdrawalRequestService;

    @GetMapping
    public String showWithdrawalPage(Model model) {
        List<WithdrawalRequest> allRequests = withdrawalRequestService.getAllRequests();
        List<WithdrawalRequest> pendingRequests = withdrawalRequestService.getPendingRequests();
        
        List<WithdrawalRequest> approvedRequests = allRequests.stream()
            .filter(r -> r.getStatus() == WithdrawalStatus.APPROVED)
            .collect(Collectors.toList());
            
        List<WithdrawalRequest> rejectedRequests = allRequests.stream()
            .filter(r -> r.getStatus() == WithdrawalStatus.REJECTED)
            .collect(Collectors.toList());

        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("allRequests", allRequests);
        model.addAttribute("approvedRequests", approvedRequests);
        model.addAttribute("rejectedRequests", rejectedRequests);

        return "Admin/Pages/Transaction/withdraw";
    }
} 