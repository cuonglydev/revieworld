package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Affiliate;
import com.example.Repository.AffiliateRepository;

@Service
public class AffiliateService {

	@Autowired
	private AffiliateRepository affiliateRepository;
	
	public Affiliate findByid(int id) {
		return affiliateRepository.findById(id).orElse(null);
	}
	
	public void save(Affiliate affiliate) {
		affiliateRepository.save(affiliate);
	}
}
