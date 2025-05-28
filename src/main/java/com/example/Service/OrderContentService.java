package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.OrderContent;
import com.example.Repository.OrderContentRepository;

@Service
public class OrderContentService {

	@Autowired
	private OrderContentRepository orderContentRepository;
	
	public List<OrderContent> findAllByOrderId(int orderId){
		return orderContentRepository.findAllByOrderId(orderId);
	}
	
	public OrderContent findById(int id) {
		return orderContentRepository.findById(id).orElse(null);
	}
	
	public void save(OrderContent orderContent) {
		orderContentRepository.save(orderContent);
	}
}
