package com.example.Service;

import java.util.List;


import org.eclipse.angus.mail.imap.protocol.ID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Language;

import com.example.Entity.OrderType;
import com.example.Repository.LanguageRepository;

@Service
public class LanguageService {

    @Autowired
    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public List<Language> getLanguagesByOrderTypeId(int id) {
        return languageRepository.findByOrderTypeId(id);
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public void saveLanguage(Language language) {
        languageRepository.save(language);
    }

    public Language getLanguageById(int id) {
        return languageRepository.findById(id).orElse(null);
    }

    public void deleteById(int id) {
        languageRepository.deleteById(id);
    }

    public boolean checkname(String name) {
        return languageRepository.existsByName(name);
    }


	
	
	
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

