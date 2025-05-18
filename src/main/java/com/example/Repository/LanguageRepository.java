package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
	List<Language> findAllByOrderTypeId(int orderTypeId);
}
