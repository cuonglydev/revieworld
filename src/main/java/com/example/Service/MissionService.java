package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Mission;
import com.example.Repository.MissionRepository;

@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;
    
    public List<Mission> findByUserId(int userId) {
        return missionRepository.findByUserId(userId);
    }
    
    public void save(Mission mission) {
        missionRepository.save(mission);
    }

    public void delete(int id) {
        missionRepository.deleteById(id);
    }
} 