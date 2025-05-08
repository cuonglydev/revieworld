package com.revieworld.controller;

import com.revieworld.model.WithdrawalRequest;
import com.revieworld.service.WithdrawalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawal-requests")
public class WithdrawalRequestController {

    @Autowired
    private WithdrawalRequestService withdrawalRequestService;

    @GetMapping("/pending")
    public ResponseEntity<List<WithdrawalRequest>> getPendingRequests() {
        return ResponseEntity.ok(withdrawalRequestService.getPendingRequests());
    }

    @GetMapping
    public ResponseEntity<List<WithdrawalRequest>> getAllRequests() {
        return ResponseEntity.ok(withdrawalRequestService.getAllRequests());
    }

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<WithdrawalRequest> approveRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(withdrawalRequestService.approveRequest(requestId));
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<WithdrawalRequest> rejectRequest(
            @PathVariable Long requestId,
            @RequestParam String reason) {
        return ResponseEntity.ok(withdrawalRequestService.rejectRequest(requestId, reason));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<WithdrawalRequest> getRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(withdrawalRequestService.getRequestById(requestId));
    }
} 