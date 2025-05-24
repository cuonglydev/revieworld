package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.OrderType;
import com.example.Entity.Support;
import com.example.Repository.SupportRepository;

@Service
public class SupportService {
    @Autowired
    private SupportRepository supportRepository;

    public Support findById(int id) {
        return supportRepository.findById(id).orElse(null);

    }

    public void SaveSupport(Support support) {
        supportRepository.save(support);

    }

    public void deleteSupport(int id) {
        supportRepository.deleteById(id);
    }

    public List<Support> getAllsSupports() {
        return supportRepository.findAll();

    }
}
