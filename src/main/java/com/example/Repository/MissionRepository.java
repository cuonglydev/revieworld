package com.example.Repository;

import com.example.Entity.Mission;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {
    List<Mission> findByStatus(String status);

    List<Mission> findByUserId(int userId);

}
