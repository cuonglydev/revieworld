package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.UserAffiliateItem;
import com.example.Repository.UserAffiliateItemRepository;

@Service
public class UserAffiliateItemService {

	@Autowired
	UserAffiliateItemRepository userAffiliateItemRepository;
	
	public List<UserAffiliateItem> findAllByUserAffiliateId(int affiliateId){
		return userAffiliateItemRepository.findAllByUserAffiliateId(affiliateId);
	}
	
	public void save(UserAffiliateItem userAffiliateItem) {
		userAffiliateItemRepository.save(userAffiliateItem);
	}
}
