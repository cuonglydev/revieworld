package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Entity.Popup;
import com.example.Repository.PopupRepository;
import java.util.List;
import java.util.Optional;

@Service
public class PopupService {
    
    @Autowired
    private PopupRepository popupRepository;
    
    public List<Popup> findAll() {
        return popupRepository.findAll();
    }
    
    public List<Popup> findActivePopups() {
        return popupRepository.findByActiveTrue();
    }
    
    public Popup save(Popup popup) {
        return popupRepository.save(popup);
    }
    
    public void delete(Long id) {
        popupRepository.deleteById(id);
    }
    
    public Optional<Popup> findById(Long id) {
        return popupRepository.findById(id);
    }
} 