package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.UserAffiliate;
import com.example.Entity.UserAffiliateItem;

import java.util.List;

@Repository
public interface UserAffiliateItemRepository extends JpaRepository<UserAffiliateItem, Integer> {
	List<UserAffiliateItem> findAllByUserAffiliateId(int id);
}
