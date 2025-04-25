package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.DefaultRank;
import com.example.Repository.DefaultRankRepository;

@Service
public class DefaultRankService {

	@Autowired
	DefaultRankRepository defaultRankRepository;
	
	public DefaultRank findById(int id) {
		return defaultRankRepository.findById(id).orElse(null);
	}
	
	public void save(DefaultRank defaultRank) {
		defaultRankRepository.save(defaultRank);
	}
	
	public void update2(DefaultRank defaultRank) {
		defaultRankRepository.save(defaultRank);
	}
}
