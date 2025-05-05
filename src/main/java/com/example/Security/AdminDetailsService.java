package com.example.Security;

import java.util.Collections;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Entity.Admin;
import com.example.Repository.AdminRepository;

@Service
public class AdminDetailsService implements UserDetailsService{
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		Admin admin = adminRepository.findByUsername(username);
		if(admin == null) {
			throw new UsernameNotFoundException("Admin không tồn tại!");
		}
		
		return new CustomAdminDetails(admin);
	}
}
