package com.example.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.OrderContent;

@Repository
public interface OrderContentRepository extends JpaRepository<OrderContent, Integer> {
	List<OrderContent> findAllByOrderId(int orderId);
}
