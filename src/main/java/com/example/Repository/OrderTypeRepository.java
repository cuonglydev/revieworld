package com.example.Repository;

import com.example.Entity.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTypeRepository extends JpaRepository<OrderType, Integer> {
	OrderType findBySlug(String slug);

	boolean existsByslug(String Slug);
}