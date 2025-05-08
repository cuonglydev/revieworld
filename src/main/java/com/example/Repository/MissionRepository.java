package com.example.Repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Mission;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {
    List<Mission> findByUserId(int userId);
} 
