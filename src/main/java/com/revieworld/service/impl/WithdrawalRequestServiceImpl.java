package com.revieworld.service.impl;

import com.revieworld.model.WithdrawalRequest;
import com.revieworld.model.WithdrawalRequest.WithdrawalStatus;
import com.revieworld.repository.WithdrawalRequestRepository;
import com.revieworld.service.WithdrawalRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WithdrawalRequestServiceImpl implements WithdrawalRequestService {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawalRequestServiceImpl.class);
    private static final BigDecimal MIN_AMOUNT = BigDecimal.valueOf(5);
    private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(500);

    @Autowired
    private WithdrawalRequestRepository withdrawalRequestRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<WithdrawalRequest> getPendingRequests() {
        logger.info("Fetching pending withdrawal requests");
        List<WithdrawalRequest> requests = withdrawalRequestRepository.findByStatus(WithdrawalStatus.PENDING);
        logger.info("Found {} pending requests", requests.size());
        return requests;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<WithdrawalRequest> getAllRequests() {
        logger.info("Fetching all withdrawal requests");
        List<WithdrawalRequest> requests = withdrawalRequestRepository.findAll();
        logger.info("Found {} total requests", requests.size());
        return requests;
    }

    @Override
    @Transactional
    public WithdrawalRequest createRequest(WithdrawalRequest request) {
        logger.info("Creating new withdrawal request for user ID: {}", request.getUserId());
        
        validateAmount(request.getAmount());
        validateBankInfo(request);
        
        request.setStatus(WithdrawalStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        
        WithdrawalRequest savedRequest = withdrawalRequestRepository.save(request);
        logger.info("Successfully created withdrawal request with ID: {}", savedRequest.getId());
        
        return savedRequest;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public WithdrawalRequest approveRequest(Long requestId) {
        logger.info("Processing approval for request ID: {}", requestId);
        WithdrawalRequest request = getRequestById(requestId);
        
        validateRequest(request);
        validateAmount(request.getAmount());
        
        request.setStatus(WithdrawalStatus.APPROVED);
        request.setProcessedAt(LocalDateTime.now());
        WithdrawalRequest savedRequest = withdrawalRequestRepository.save(request);
        logger.info("Successfully approved request ID: {}", requestId);
        
        return savedRequest;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public WithdrawalRequest rejectRequest(Long requestId, String reason) {
        logger.info("Processing rejection for request ID: {} with reason: {}", requestId, reason);
        WithdrawalRequest request = getRequestById(requestId);
        
        validateRequest(request);
        if (reason == null || reason.trim().isEmpty()) {
            logger.error("Rejection reason is required for request ID: {}", requestId);
            throw new IllegalArgumentException("Rejection reason is required");
        }
        
        request.setStatus(WithdrawalStatus.REJECTED);
        request.setRejectionReason(reason);
        request.setProcessedAt(LocalDateTime.now());
        WithdrawalRequest savedRequest = withdrawalRequestRepository.save(request);
        logger.info("Successfully rejected request ID: {}", requestId);
        
        return savedRequest;
    }

    @Override
    public WithdrawalRequest getRequestById(Long requestId) {
        logger.debug("Fetching withdrawal request with ID: {}", requestId);
        return withdrawalRequestRepository.findById(requestId)
            .orElseThrow(() -> {
                logger.error("Withdrawal request not found with ID: {}", requestId);
                return new RuntimeException("Withdrawal request not found");
            });
    }

    private void validateRequest(WithdrawalRequest request) {
        if (request == null) {
            throw new RuntimeException("Withdrawal request not found");
        }
        if (request.getStatus() != WithdrawalStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be processed");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(MIN_AMOUNT) < 0 || amount.compareTo(MAX_AMOUNT) > 0) {
            throw new IllegalArgumentException(
                String.format("Amount must be between $%s and $%s", MIN_AMOUNT, MAX_AMOUNT)
            );
        }
    }

    private void validateBankInfo(WithdrawalRequest request) {
        if (request.getBankName() == null || request.getBankName().trim().isEmpty()) {
            throw new IllegalArgumentException("Bank name is required");
        }
        if (request.getBankAccount() == null || request.getBankAccount().trim().isEmpty()) {
            throw new IllegalArgumentException("Bank account number is required");
        }
    }
} 