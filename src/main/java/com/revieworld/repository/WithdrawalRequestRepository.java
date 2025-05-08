package com.revieworld.repository;

import com.revieworld.model.WithdrawalRequest;
import com.revieworld.model.WithdrawalRequest.WithdrawalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Repository
public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, Long> {
    @Cacheable("pendingRequests")
    List<WithdrawalRequest> findByStatus(WithdrawalStatus status);
    
    @Cacheable("userRequests")
    List<WithdrawalRequest> findByUserId(Long userId);
    
    Page<WithdrawalRequest> findByStatus(WithdrawalStatus status, Pageable pageable);
    
    Page<WithdrawalRequest> findByUserId(Long userId, Pageable pageable);
} 