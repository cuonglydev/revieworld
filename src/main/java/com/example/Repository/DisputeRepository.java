package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Dispute;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, Integer> {
    List<Dispute> findByUserId(int userId);
    List<Dispute> findByMissionId(int missionId);
} 