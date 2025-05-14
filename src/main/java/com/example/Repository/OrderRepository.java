package com.example.Repository;

import com.example.Entity.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	List<Order> findAllByUserId(int userId);
	List<Order> findAllByUserIdAndStatus(int userId, String status);
}
