package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.DefaultWithdraw;
import com.example.Repository.DefaultwithdrawRepository;

@Service
public class defaultwithdrawService {
    @Autowired
    private DefaultwithdrawRepository defaultwithdrawRepository;

    public DefaultWithdraw findById(int id) {
        return defaultwithdrawRepository.findById(id).orElse(null);
    }

    public void save(DefaultWithdraw defaultWithdraw) {
        defaultwithdrawRepository.save(defaultWithdraw);
    }
}
