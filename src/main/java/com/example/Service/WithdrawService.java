package com.example.Service;

import java.util.List;
import java.util.Date;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Withdraw;
import com.example.Repository.WithdrawRepository;
import com.example.Entity.User;

@Service
public class WithdrawService {

    @Autowired
    private WithdrawRepository withdrawRepository;
    
    @Autowired
    private UserService userService;
    
    public List<Withdraw> findByUserId(int userId) {
        return withdrawRepository.findByUserId(userId);
    }
    
    public void save(Withdraw withdraw) {
        withdrawRepository.save(withdraw);
    }
    
    public void delete(int id) {
        withdrawRepository.deleteById(id);
    }
    
    public List<Withdraw> findByStatus(String status) {
        return withdrawRepository.findByStatus(status);
    }
    
    public List<Withdraw> findAll() {
        return withdrawRepository.findAll();
    }

    public void approveWithdrawal(int id) {
        Withdraw withdraw = withdrawRepository.findById(id).orElse(null);
        if (withdraw != null && "PENDING".equals(withdraw.getStatus())) {
            User user = withdraw.getUser();
            if (user != null && user.getBonusAmount() != null && user.getBonusAmount() >= withdraw.getAmount()) {
                user.setBonusAmount(user.getBonusAmount() - withdraw.getAmount());
                userService.save(user);
                withdraw.setStatus("APPROVED");
                withdraw.setProcessedAt(new Date());
                withdrawRepository.save(withdraw);
            }
             
        }
    }

    public void rejectWithdrawal(int id, String reason) {
        Withdraw withdraw = withdrawRepository.findById(id).orElse(null);
        if (withdraw != null && "PENDING".equals(withdraw.getStatus())) {
            // Get user and refund the amount to their bonus_amount
            User user = withdraw.getUser();
            if (user != null) {
                user.setBonusAmount(user.getBonusAmount() + withdraw.getAmount());
                userService.save(user);
            }
            
            withdraw.setStatus("REJECTED");
            withdraw.setNote(reason);
            withdraw.setProcessedAt(new Date());
            withdrawRepository.save(withdraw);
        }
    }

    public List<Withdraw> findApprovedByUserId(int userId) {
        return withdrawRepository.findByUserIdAndStatusIn(userId, Arrays.asList("APPROVED", "SUCCESS"));
    }
} 