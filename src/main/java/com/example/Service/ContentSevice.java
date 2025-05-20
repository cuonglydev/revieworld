package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Content;
import com.example.Repository.ContentRepository;

@Service
public class ContentSevice {
    @Autowired
    private ContentRepository contentRepository;

    public void saveContent(Content content) {
        contentRepository.save(content);
    }

    public void deleteContent(int id) {
        contentRepository.deleteById(id);
    }

    public Content getContentById(int id) {
        return contentRepository.findById(id);
    }

    public List<Content> getAllContent() {
        return contentRepository.findAll();
    }
}
