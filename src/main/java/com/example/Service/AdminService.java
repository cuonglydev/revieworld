package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Entity.Admin;
import com.example.Repository.AdminRepository;

@Service
public class AdminService {

	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Admin findByUsername(String username) {
		return adminRepository.findByUsername(username);
	}
	
	public Admin findById(int id) {
		return adminRepository.findById(id).orElse(null);
	}
	
	public List<Admin> findAll(){
		return adminRepository.findAll();
	}
	
	public void save(Admin admin) {
		adminRepository.save(admin);
	}
	
	public void deleteById(int id) {
		adminRepository.deleteById(id);
	}
	
	
}
