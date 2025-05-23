package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Entity.Affiliate;
import com.example.Entity.User;
import com.example.Entity.UserAffiliate;
import com.example.Repository.AffiliateRepository;
import com.example.Repository.UserAffiliateRepository;

import java.util.Date;

@Service
public class AffiliateService {

	@Autowired
	private AffiliateRepository affiliateRepository;
	
	@Autowired
	private UserAffiliateRepository userAffiliateRepository;
	
	public Affiliate findByid(int id) {
		return affiliateRepository.findById(id).orElse(null);
	}
	
	@Transactional
	public Affiliate saveAffiliate(Affiliate affiliate) {
		// Validate commission percentages
		validateCommissionPercentages(affiliate);
		
		// Set dates if not set
		if (affiliate.getStartDate() == null) {
			affiliate.setStartDate(new Date());
		}
		
		return affiliateRepository.save(affiliate);
	}
	
	private void validateCommissionPercentages(Affiliate affiliate) {
		if (affiliate.getBaseCommissionPercentage() < 0 || affiliate.getBaseCommissionPercentage() > 100) {
			throw new IllegalArgumentException("Tỷ lệ hoa hồng cơ bản phải từ 0-100%");
		}
		if (affiliate.getNewUserCommissionPercentage() < 0 || affiliate.getNewUserCommissionPercentage() > 100) {
			throw new IllegalArgumentException("Tỷ lệ hoa hồng người dùng mới phải từ 0-100%");
		}
		if (affiliate.getReferralCommissionPercentage() < 0 || affiliate.getReferralCommissionPercentage() > 100) {
			throw new IllegalArgumentException("Tỷ lệ hoa hồng người được giới thiệu phải từ 0-100%");
		}
	}
	
	public Double calculateCommission(User user, Double amount, String commissionType) {
		Affiliate affiliate = findByid(1); // Get default affiliate settings
		if (affiliate == null || !affiliate.getStatus()) {
			return 0.0;
		}

		// Check minimum amount
		if (amount < affiliate.getMinCommissionAmount()) {
			return 0.0;
		}

		Double commissionRate;
		switch (commissionType) {
			case "NEW_USER":
				commissionRate = affiliate.getNewUserCommissionPercentage();
				break;
			case "REFERRAL":
				commissionRate = affiliate.getReferralCommissionPercentage();
				break;
			default:
				commissionRate = affiliate.getBaseCommissionPercentage();
		}

		Double commission = amount * (commissionRate / 100);

		// Cap at maximum commission amount
		return Math.min(commission, affiliate.getMaxCommissionAmount());
	}
	
	public boolean isEligibleForCommission(UserAffiliate userAffiliate) {
		Affiliate affiliate = findByid(1);
		if (affiliate == null || !affiliate.getStatus()) {
			return false;
		}

		// Check if program is active
		Date now = new Date();
		if (affiliate.getEndDate() != null && now.after(affiliate.getEndDate())) {
			return false;
		}

		// Check waiting period
		if (affiliate.getCommissionWaitingDays() > 0) {
			Date creationDate = userAffiliate.getCreatedAt();
			if (creationDate != null) {
				long diffInDays = (now.getTime() - creationDate.getTime()) / (1000 * 60 * 60 * 24);
				return diffInDays >= affiliate.getCommissionWaitingDays();
			}
		}

		return true;
	}
}
