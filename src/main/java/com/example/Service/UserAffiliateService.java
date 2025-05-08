package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.UserAffiliate;
import com.example.Repository.UserAffiliateRepository;

@Service
public class UserAffiliateService {

	@Autowired
	private UserAffiliateRepository userAffiliateRepository;
	
	public UserAffiliate findById(int id) {
		return userAffiliateRepository.findById(id).orElse(null);
	}
	
	public List<UserAffiliate> findAllByUserId(int userId){
		return userAffiliateRepository.findAllByUserId(userId);
	}
	
	public void save(UserAffiliate userAffiliate) {
		userAffiliateRepository.save(userAffiliate);
	}
	
	public void delete(int id) {
		userAffiliateRepository.deleteById(id);
	}

}
