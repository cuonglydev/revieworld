package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Dispute;
import com.example.Repository.DisputeRepository;

@Service
public class DisputeService {
    @Autowired
    private DisputeRepository disputeRepository;

    public List<Dispute> findAll() {
        return disputeRepository.findAll();
    }

    public Dispute findById(int id) {
        return disputeRepository.findById(id).orElse(null);
    }

    public List<Dispute> findByUserId(int userId) {
        return disputeRepository.findByUserId(userId);
    }

    public List<Dispute> findByMissionId(int missionId) {
        return disputeRepository.findByMissionId(missionId);
    }

    public void save(Dispute dispute) {
        disputeRepository.save(dispute);
    }

    public void delete(int id) {
        disputeRepository.deleteById(id);
    }
} 