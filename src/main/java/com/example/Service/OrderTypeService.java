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

    public boolean checkeachother(String slug) {
        return orderTypeRepository.existsByslug(slug);
    }

    public OrderType findBySlug(String slug) {
        return orderTypeRepository.findBySlug(slug);
    }

    public static String convertToSlugFormat(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String slug = input.toLowerCase();

        slug = java.text.Normalizer.normalize(slug, java.text.Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        slug = slug.replaceAll("\\s+", "_");
        slug = slug.replaceAll("[^a-z0-9_]", "");

        return slug;

    }

    public void UpdeadOrdertype1(OrderType ordertype) {
        OrderType existingOrderType = getOrderTypeById(ordertype.getId())
                .orElseThrow(() -> new RuntimeException("OrderType not found"));
        existingOrderType.setName(ordertype.getName());
        existingOrderType.setFeePercentage(ordertype.getFeePercentage());
        existingOrderType.setSlug(ordertype.getSlug());
        existingOrderType.setUrl(ordertype.getUrl());
        existingOrderType.setLink(ordertype.getLink());
        existingOrderType.setCreatedAt(ordertype.getCreatedAt());
        saveOrderType(existingOrderType);
    }
}