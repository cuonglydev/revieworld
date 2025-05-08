package com.example.Service;

import com.example.Entity.UserBank;
import com.example.Repository.UserBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserBankService {
    private static final Logger logger = LoggerFactory.getLogger(UserBankService.class);

    @Autowired
    private UserBankRepository userBankRepository;

    @Transactional(readOnly = true)
    public List<UserBank> findAll() {
        try {
            logger.debug("Finding all banks");
            return userBankRepository.findAll();
        } catch (Exception e) {
            logger.error("Error finding all banks: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể lấy danh sách ngân hàng", e);
        }
    }

    @Transactional(readOnly = true)
    public List<UserBank> findByUserId(int userId) {
        logger.debug("Finding banks for user ID: {}", userId);
        try {
            List<UserBank> banks = userBankRepository.findByUserId(userId);
            logger.debug("Found {} banks for user ID: {}", banks.size(), userId);
            return banks;
        } catch (Exception e) {
            logger.error("Error finding banks for user ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Không thể lấy danh sách ngân hàng", e);
        }
    }

    @Transactional
    public UserBank save(UserBank userBank) {
        try {
            logger.info("=== Starting to save bank account ===");
            logger.debug("Input bank account data: {}", userBank);
            
            // Validate user
            if (userBank.getUser() == null) {
                logger.error("Cannot save bank account: user is null");
                throw new IllegalArgumentException("User không được để trống");
            }
            logger.debug("User validation passed");
            
            // Validate method
            if (userBank.getMethod() == null || userBank.getMethod().trim().isEmpty()) {
                logger.error("Cannot save bank account: method is null or empty");
                throw new IllegalArgumentException("Phương thức không được để trống");
            }
            logger.debug("Method validation passed: {}", userBank.getMethod());
            
            // Validate account number
            if (userBank.getAccountNumber() == null || userBank.getAccountNumber().trim().isEmpty()) {
                logger.error("Cannot save bank account: account number is null or empty");
                throw new IllegalArgumentException("Số tài khoản không được để trống");
            }
            logger.debug("Account number validation passed: {}", userBank.getAccountNumber());
            
            // Validate bank-specific fields
            if ("BANK".equals(userBank.getMethod().trim())) {
                if (userBank.getBankName() == null || userBank.getBankName().trim().isEmpty()) {
                    logger.error("Cannot save bank account: bank name is null or empty");
                    throw new IllegalArgumentException("Tên ngân hàng không được để trống");
                }
                if (userBank.getAccountName() == null || userBank.getAccountName().trim().isEmpty()) {
                    logger.error("Cannot save bank account: account name is null or empty");
                    throw new IllegalArgumentException("Tên tài khoản không được để trống");
                }
                // Validate account number format for bank accounts
                if (!userBank.getAccountNumber().trim().matches("^\\d+$")) {
                    logger.error("Cannot save bank account: invalid account number format");
                    throw new IllegalArgumentException("Số tài khoản chỉ được chứa số");
                }
                logger.debug("Bank-specific validations passed");
            } else if ("USDT".equals(userBank.getMethod().trim())) {
                // Validate USDT address format
                if (!userBank.getAccountNumber().trim().matches("^[a-zA-Z0-9]{30,}$")) {
                    logger.error("Cannot save bank account: invalid USDT address format");
                    throw new IllegalArgumentException("Địa chỉ ví USDT không hợp lệ");
                }
                logger.debug("USDT-specific validations passed");
            } else {
                logger.error("Cannot save bank account: invalid method {}", userBank.getMethod());
                throw new IllegalArgumentException("Phương thức không hợp lệ");
            }
            
            // Check for duplicate account number
            if (userBank.getId() == 0 && existsByUserIdAndAccountNumber(userBank.getUser().getId(), userBank.getAccountNumber().trim())) {
                logger.error("Cannot save bank account: duplicate account number");
                throw new IllegalArgumentException("BANK".equals(userBank.getMethod()) ? 
                    "Số tài khoản này đã tồn tại" : 
                    "Địa chỉ ví này đã tồn tại");
            }
            logger.debug("Duplicate check passed");
            
            // Trim all string fields
            userBank.setMethod(userBank.getMethod().trim());
            userBank.setAccountNumber(userBank.getAccountNumber().trim());
            if (userBank.getBankName() != null) {
                userBank.setBankName(userBank.getBankName().trim());
            }
            if (userBank.getAccountName() != null) {
                userBank.setAccountName(userBank.getAccountName().trim());
            }
            logger.debug("Data prepared for save: {}", userBank);
            
            // Attempt to save
            logger.info("Attempting to save bank account to database");
            UserBank savedBank = userBankRepository.save(userBank);
            logger.info("Successfully saved bank account with ID: {}", savedBank.getId());
            return savedBank;
        } catch (IllegalArgumentException e) {
            logger.error("Validation error while saving bank account: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error saving bank account: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể lưu thông tin ngân hàng: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<UserBank> findById(int id) {
        try {
            logger.debug("Finding bank by ID: {}", id);
            Optional<UserBank> bank = userBankRepository.findById(id);
            if (bank.isPresent()) {
                logger.debug("Found bank with ID: {}", id);
            } else {
                logger.debug("No bank found with ID: {}", id);
            }
            return bank;
        } catch (Exception e) {
            logger.error("Error finding bank with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Không thể tìm thấy thông tin ngân hàng", e);
        }
    }

    @Transactional
    public void deleteById(int id) {
        try {
            logger.debug("Deleting bank with ID: {}", id);
            userBankRepository.deleteById(id);
            logger.info("Successfully deleted bank with ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting bank with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Không thể xóa thông tin ngân hàng", e);
        }
    }

    @Transactional(readOnly = true)
    public boolean existsByUserIdAndAccountNumber(int userId, String accountNumber) {
        try {
            logger.debug("Checking if bank account exists for user ID: {} and account number: {}", userId, accountNumber);
            boolean exists = userBankRepository.existsByUserIdAndAccountNumber(userId, accountNumber.trim());
            logger.debug("Bank account exists: {}", exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking bank account existence: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể kiểm tra tồn tại của tài khoản ngân hàng", e);
        }
    }
} 