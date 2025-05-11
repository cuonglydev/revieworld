package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.AdminBank;

@Repository
public interface AdminBankRepository extends JpaRepository<AdminBank, Integer> {
	
}
