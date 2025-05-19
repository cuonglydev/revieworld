package com.example.Repository;

import com.example.Entity.Popups;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PopupsRepository extends JpaRepository<Popups, Integer> {
    List<Popups> findByStatusTrue();
} 