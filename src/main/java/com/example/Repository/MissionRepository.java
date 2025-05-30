package com.example.Repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Mission;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {
    List<Mission> findAllByUserId(int userId);
    List<Mission> findAllByOrderId(int orderId);
    List<Mission> findAllByOrderIdAndStatus(int orderId, String status);
    List<Mission> findAllByOrderIdAndStatusIn(int orderId, List<String> statuses);
    Mission findByOrderIdAndUserId(int orderId, int userId);
} 
