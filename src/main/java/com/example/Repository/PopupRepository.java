package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Entity.Popup;
import java.util.List;

@Repository
public interface PopupRepository extends JpaRepository<Popup, Long> {
    List<Popup> findByActiveTrue();
} 