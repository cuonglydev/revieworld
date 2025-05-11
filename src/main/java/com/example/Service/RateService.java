package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Rate;
import com.example.Repository.RateRepository;

@Service
public class RateService {

	@Autowired
	RateRepository rateRepository;
	
	public Rate findById(int id) {
		return rateRepository.findById(id).orElse(null);
	}
	
	public void save(Rate rate) {
		rateRepository.save(rate);
	}
	
	public Rate findByCountry(String country){
		return rateRepository.findByCountry(country);
	}
}
