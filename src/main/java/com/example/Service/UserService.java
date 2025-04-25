package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Entity.User;
import com.example.Repository.UserRepository;


@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> findAll(){
		return userRepository.findAll(); 
	}
	
	public User findById(int id) {
		return userRepository.findById(id).orElse(null);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User findByInviteCode(String inviteCode) {
		return userRepository.findByInviteCode(inviteCode);
	}
	
	public void save(User user) {
		userRepository.save(user);
	}
	
	public void deleteById(int id) {
		userRepository.deleteById(id);
	}
	
}
