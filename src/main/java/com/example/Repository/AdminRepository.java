package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
	Admin findByUsername(String username); 
}
