package com.example.Service;

import com.example.Entity.OrderType;
import com.example.Repository.OrderTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderTypeService {

    @Autowired
    private OrderTypeRepository orderTypeRepository;

    public List<OrderType> getAllOrderTypes() {
        return orderTypeRepository.findAll();
    }

    public Optional<OrderType> getOrderTypeById(int id) {
        return orderTypeRepository.findById(id);
    }

    public void saveOrderType(OrderType orderType) {
        orderTypeRepository.save(orderType);
    }

    public void deleteOrderType(int id) {
        orderTypeRepository.deleteById(id);
    }
}