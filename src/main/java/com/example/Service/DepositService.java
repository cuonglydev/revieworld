package com.example.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Entity.Deposit;
import com.example.Entity.User;
import com.example.Entity.UserAffiliate;
import com.example.Repository.DepositRepository;
import com.example.Repository.UserAffiliateRepository;

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;
    
    @Autowired
    private UserAffiliateRepository userAffiliateRepository;
    
    @Autowired
    private UserService userService;
    
    public Deposit findById(int id) {
    	return depositRepository.findById(id).orElse(null);
    }
    
    public List<Deposit> findByUserId(int userId) {
        return depositRepository.findByUserId(userId);
    }
    
	/*
	 * @Transactional public Deposit processDeposit(Deposit deposit) { // Set
	 * deposit status and creation time deposit.setStatus("PENDING");
	 * deposit.setCreatedAt(new Date());
	 * 
	 * // Save deposit record Deposit savedDeposit =
	 * depositRepository.save(deposit);
	 * 
	 * // If deposit is successful, update user balance and handle commission if
	 * (savedDeposit.getStatus().equals("SUCCESS")) { User user = deposit.getUser();
	 * 
	 * // Update user's main wallet balance user.setAmount(user.getAmount() +
	 * deposit.getAmount()); user.setTotalDeposited(user.getTotalDeposited() +
	 * deposit.getAmount()); userService.save(user);
	 * 
	 * // Handle commission for referrer if exists handleCommission(user,
	 * deposit.getAmount()); }
	 * 
	 * return savedDeposit; }
	 */
	/*
	 * private void handleCommission(User user, Double depositAmount) { // Find
	 * referrer through UserAffiliate List<UserAffiliate> userAffiliates =
	 * userAffiliateRepository.findAllByUserId(user.getId()); UserAffiliate
	 * userAffiliate = userAffiliates.isEmpty() ? null : userAffiliates.get(0); if
	 * (userAffiliate != null && userAffiliate.getReferrer() != null) { User
	 * referrer = userAffiliate.getReferrer();
	 * 
	 * // Calculate commission (5% of deposit amount) Double commission =
	 * depositAmount * 0.05;
	 * 
	 * // Update referrer's bonus wallet
	 * referrer.setBonusAmount(referrer.getBonusAmount() + commission);
	 * userService.save(referrer); } }
	 */
    public void save(Deposit deposit) {
        depositRepository.save(deposit);
    }
    
    public void delete(int id) {
        depositRepository.deleteById(id);
    }
    
    public Deposit findByTransactionId(String transactionId) {
        return depositRepository.findByTransactionId(transactionId);
    }
    
    public Deposit updateStatusById(int id, String status) {
    	Deposit deposit = depositRepository.findById(id).orElse(null);
    	if(deposit != null) {
    		deposit.setStatus(status);
    		depositRepository.save(deposit);
    	}else {
    		return null;
		}
    	return deposit;
    }
} 