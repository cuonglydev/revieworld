package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.UserAffiliate;

@Repository
public interface UserAffiliateRepository extends JpaRepository<UserAffiliate, Integer> {
	List<UserAffiliate> findAllByUserId(int userId);
}
