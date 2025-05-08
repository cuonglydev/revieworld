package com.revieworld.service;

import com.revieworld.model.WithdrawalRequest;
import com.revieworld.model.WithdrawalRequest.WithdrawalStatus;
import com.revieworld.repository.WithdrawalRequestRepository;
import com.revieworld.service.impl.WithdrawalRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WithdrawalRequestServiceTest {

    @Mock
    private WithdrawalRequestRepository withdrawalRequestRepository;

    @InjectMocks
    private WithdrawalRequestServiceImpl withdrawalRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRequest_ValidRequest_Success() {
        // Arrange
        WithdrawalRequest request = new WithdrawalRequest();
        request.setUserId(1L);
        request.setAmount(BigDecimal.valueOf(100));
        request.setBankName("Test Bank");
        request.setBankAccount("1234567890");

        when(withdrawalRequestRepository.save(any(WithdrawalRequest.class)))
            .thenAnswer(invocation -> {
                WithdrawalRequest savedRequest = invocation.getArgument(0);
                savedRequest.setId(1L);
                return savedRequest;
            });

        // Act
        WithdrawalRequest result = withdrawalRequestService.createRequest(request);

        // Assert
        assertNotNull(result);
        assertEquals(WithdrawalStatus.PENDING, result.getStatus());
        assertNotNull(result.getCreatedAt());
        verify(withdrawalRequestRepository).save(any(WithdrawalRequest.class));
    }

    @Test
    void createRequest_InvalidAmount_ThrowsException() {
        // Arrange
        WithdrawalRequest request = new WithdrawalRequest();
        request.setAmount(BigDecimal.valueOf(1));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            withdrawalRequestService.createRequest(request);
        });
    }

    @Test
    void approveRequest_ValidRequest_Success() {
        // Arrange
        WithdrawalRequest request = new WithdrawalRequest();
        request.setId(1L);
        request.setStatus(WithdrawalStatus.PENDING);
        request.setAmount(BigDecimal.valueOf(100));

        when(withdrawalRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(withdrawalRequestRepository.save(any(WithdrawalRequest.class))).thenReturn(request);

        // Act
        WithdrawalRequest result = withdrawalRequestService.approveRequest(1L);

        // Assert
        assertEquals(WithdrawalStatus.APPROVED, result.getStatus());
        assertNotNull(result.getProcessedAt());
        verify(withdrawalRequestRepository).save(any(WithdrawalRequest.class));
    }

    @Test
    void rejectRequest_ValidRequest_Success() {
        // Arrange
        WithdrawalRequest request = new WithdrawalRequest();
        request.setId(1L);
        request.setStatus(WithdrawalStatus.PENDING);
        request.setAmount(BigDecimal.valueOf(100));

        when(withdrawalRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(withdrawalRequestRepository.save(any(WithdrawalRequest.class))).thenReturn(request);

        // Act
        WithdrawalRequest result = withdrawalRequestService.rejectRequest(1L, "Test reason");

        // Assert
        assertEquals(WithdrawalStatus.REJECTED, result.getStatus());
        assertEquals("Test reason", result.getRejectionReason());
        assertNotNull(result.getProcessedAt());
        verify(withdrawalRequestRepository).save(any(WithdrawalRequest.class));
    }

    @Test
    void rejectRequest_NoReason_ThrowsException() {
        // Arrange
        WithdrawalRequest request = new WithdrawalRequest();
        request.setId(1L);
        request.setStatus(WithdrawalStatus.PENDING);

        when(withdrawalRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            withdrawalRequestService.rejectRequest(1L, null);
        });
    }
} 