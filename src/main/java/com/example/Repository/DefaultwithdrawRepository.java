package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.DefaultWithdraw;

@Repository
public interface DefaultwithdrawRepository extends JpaRepository<DefaultWithdraw, Integer> {
    // Custom query methods can be defined here if needed

}
