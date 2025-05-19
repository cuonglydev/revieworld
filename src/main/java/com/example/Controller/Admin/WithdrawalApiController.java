package com.example.Controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Service.WithdrawService;

@RestController
@RequestMapping("/api/withdrawal-requests")
public class WithdrawalApiController {

    @Autowired
    private WithdrawService withdrawService;

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable int id) {
        withdrawService.approveWithdrawal(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable int id, @RequestParam String reason) {
        withdrawService.rejectWithdrawal(id, reason);
        return ResponseEntity.ok().build();
    }
} 