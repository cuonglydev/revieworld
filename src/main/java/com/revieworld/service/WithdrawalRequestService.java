package com.revieworld.service;

import com.revieworld.model.WithdrawalRequest;
import java.util.List;

public interface WithdrawalRequestService {
    List<WithdrawalRequest> getPendingRequests();
    List<WithdrawalRequest> getAllRequests();
    WithdrawalRequest createRequest(WithdrawalRequest request);
    WithdrawalRequest approveRequest(Long requestId);
    WithdrawalRequest rejectRequest(Long requestId, String reason);
    WithdrawalRequest getRequestById(Long requestId);
} 