package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.AdminBank;
import com.example.Repository.AdminBankRepository;

@Service
public class AdminBankService {

	@Autowired
	AdminBankRepository adminBankRepository;
	
	public AdminBank findById(int id) {
		return adminBankRepository.findById(id).orElse(null);
	}
	
	public void save(AdminBank adminBank) {
		adminBankRepository.save(adminBank);
	}
}
