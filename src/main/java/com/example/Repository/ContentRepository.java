package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {

    // Tìm kiếm theo tiêu đề
    Content findByTitle(String title);

    // Tìm kiếm theo id
    Content findById(int id);

    // Xóa theo id
    void deleteById(int id);

    // Lưu nội dung

}
