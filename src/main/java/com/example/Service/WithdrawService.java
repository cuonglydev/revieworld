package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Withdraw;
import com.example.Repository.WithdrawRepository;

@Service
public class WithdrawService {

    @Autowired
    private WithdrawRepository withdrawRepository;
    
    public List<Withdraw> findByUserId(int userId) {
        return withdrawRepository.findByUserId(userId);
    }
    
    public void save(Withdraw withdraw) {
        withdrawRepository.save(withdraw);
    }
    
    public void delete(int id) {
        withdrawRepository.deleteById(id);
    }
} 