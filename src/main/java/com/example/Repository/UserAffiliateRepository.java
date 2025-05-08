package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.UserAffiliate;
import java.util.List;

@Repository
public interface UserAffiliateRepository extends JpaRepository<UserAffiliate, Integer> {
	List<UserAffiliate> findAllByUserId(int userId);
}
