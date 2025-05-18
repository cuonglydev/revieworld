package com.example.Repository;

import java.util.List;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Deposit;
import com.example.Entity.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
    List<Language> findByOrderTypeId(int orderTypeId); // lấy danh sách ngôn ngữ theo id của ordertype

    boolean existsByName(String name);
}