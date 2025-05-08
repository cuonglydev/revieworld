package com.revieworld.repository;

import com.revieworld.model.Popup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PopupRepository extends JpaRepository<Popup, Long> {
    List<Popup> findByActiveTrue();
} 