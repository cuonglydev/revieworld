package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Language;
import com.example.Repository.LanguageRepository;

@Service
public class LanguageService {
	
	@Autowired
	private LanguageRepository languageRepository;
	
	public List<Language> findAll() {
		return languageRepository.findAll();
	}
	
	public List<Language> findAllByOrderTypeId(int orderTypeId){
		return languageRepository.findAllByOrderTypeId(orderTypeId);
	}
	
	public Language findById(int id) {
		return languageRepository.findById(id).orElse(null);
	}
}
