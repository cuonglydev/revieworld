package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.OrderPhoto;
import com.example.Repository.OrderPhotoRepository;

@Service
public class OrderPhotoService {

	@Autowired
	OrderPhotoRepository orderPhotoRepository;
	
	public OrderPhoto findById(int id) {
		return orderPhotoRepository.findById(id).orElse(null);
	}
	
	public List<OrderPhoto> findAllByIOrderId(int orderId){
		return orderPhotoRepository.findAllByOrderId(orderId);
	}
	
	public void save(OrderPhoto orderPhoto) {
		orderPhotoRepository.save(orderPhoto);
	}
	
	public void deleteById(int id) {
		orderPhotoRepository.deleteById(id);
	}
}
