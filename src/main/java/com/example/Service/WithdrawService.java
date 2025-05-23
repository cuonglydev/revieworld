package com.example.Service;

import java.util.List;
import java.util.Date;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Entity.Withdraw;
import com.example.Repository.WithdrawRepository;
import com.example.Entity.User;
<<<<<<< Updated upstream
//import com.example.Exception.WithdrawException;
=======
>>>>>>> Stashed changes

@Service
public class WithdrawService {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawService.class);

    @Autowired
    private WithdrawRepository withdrawRepository;

    @Autowired
    private UserService userService;

    public List<Withdraw> findByUserId(int userId) {
        return withdrawRepository.findByUserId(userId);
    }

    public void save(Withdraw withdraw) {
        withdrawRepository.save(withdraw);
    }

    public void delete(int id) {
        withdrawRepository.deleteById(id);
    }

    public List<Withdraw> findByStatus(String status) {
        return withdrawRepository.findByStatus(status);
    }

    public List<Withdraw> findAll() {
        return withdrawRepository.findAll();
    }

    public Withdraw findById(int id) {
<<<<<<< Updated upstream
    	return withdrawRepository.findById(id).orElse(null);
    }
    
//    @Transactional
//    public void approveWithdrawal(int id) {
//        try {
//            Withdraw withdraw = withdrawRepository.findById(id).orElse(null);
//            
//            if (!"WAITING".equals(withdraw.getStatus())) {
//               return 0;
//            }
//
//            User user = withdraw.getUser();
//            if (user == null) {
//                throw new WithdrawException("Không tìm thấy thông tin người dùng");
//            }
//
//            if (user.getBonusAmount() == null || user.getBonusAmount() < withdraw.getAmount()) {
//                throw new WithdrawException("Số dư không đủ để thực hiện giao dịch");
//            }
//
//            // Update user balance
//            user.setBonusAmount(user.getBonusAmount() - withdraw.getAmount());
//            userService.save(user);
//
//            // Update withdraw status
//            withdraw.setStatus("APPROVED");
//            withdraw.setProcessedAt(new Date());
//            withdrawRepository.save(withdraw);
//
//            logger.info("Withdrawal approved successfully - ID: {}, Amount: {}, User: {}", 
//                id, withdraw.getAmount(), user.getId());
//                
//        } catch (Exception e) {
//            logger.error("Error approving withdrawal - ID: {}", id, e);
//            throw new WithdrawException("Lỗi khi xử lý yêu cầu rút tiền: " + e.getMessage());
//        }
//    }
//
//    @Transactional
//    public void rejectWithdrawal(int id, String reason) {
//        try {
//            Withdraw withdraw = withdrawRepository.findById(id).orElse(null);
//            
//            if (!"PENDING".equals(withdraw.getStatus())) {
//                throw new WithdrawException("Trạng thái yêu cầu không hợp lệ");
//            }
//
//            User user = withdraw.getUser();
//            if (user != null) {
//                // Refund amount to user
//                user.setBonusAmount(user.getBonusAmount() + withdraw.getAmount());
//                userService.save(user);
//            }
//            
//            withdraw.setStatus("REJECTED");
//            withdraw.setNote(reason);
//            withdraw.setProcessedAt(new Date());
//            withdrawRepository.save(withdraw);
//
//            logger.info("Withdrawal rejected - ID: {}, Reason: {}, User: {}", 
//                id, reason, user != null ? user.getId() : "unknown");
//                
//        } catch (Exception e) {
//            logger.error("Error rejecting withdrawal - ID: {}", id, e);
//            throw new WithdrawException("Lỗi khi từ chối yêu cầu rút tiền: " + e.getMessage());
//        }
//    }
//
//    public List<Withdraw> findApprovedByUserId(int userId) {
//        return withdrawRepository.findByUserIdAndStatusIn(userId, Arrays.asList("APPROVED", "SUCCESS"));
//    }
} 
=======
        return withdrawRepository.findById(id).orElse(null);
    }

    // @Transactional
    // public void approveWithdrawal(int id) {
    // try {
    // Withdraw withdraw = withdrawRepository.findById(id).orElse(null);

    // if (!"WAITING".equals(withdraw.getStatus())) {
    // return 0;
    // }

    // User user = withdraw.getUser();
    // if (user == null) {
    // throw new WithdrawException("Không tìm thấy thông tin người dùng");
    // }

    // if (user.getBonusAmount() == null || user.getBonusAmount() <
    // withdraw.getAmount()) {
    // throw new WithdrawException("Số dư không đủ để thực hiện giao dịch");
    // }

    // // Update user balance
    // user.setBonusAmount(user.getBonusAmount() - withdraw.getAmount());
    // userService.save(user);

    // // Update withdraw status
    // withdraw.setStatus("APPROVED");
    // withdraw.setProcessedAt(new Date());
    // withdrawRepository.save(withdraw);

    // logger.info("Withdrawal approved successfully - ID: {}, Amount: {}, User:
    // {}",
    // id, withdraw.getAmount(), user.getId());

    // } catch (Exception e) {
    // logger.error("Error approving withdrawal - ID: {}", id, e);
    // throw new WithdrawException("Lỗi khi xử lý yêu cầu rút tiền: " +
    // e.getMessage());
    // }
    // }

    // @Transactional
    // public void rejectWithdrawal(int id, String reason) {
    // try {
    // Withdraw withdraw = withdrawRepository.findById(id).orElse(null);

    // if (!"PENDING".equals(withdraw.getStatus())) {
    // throw new WithdrawException("Trạng thái yêu cầu không hợp lệ");
    // }

    // User user = withdraw.getUser();
    // if (user != null) {
    // // Refund amount to user
    // user.setBonusAmount(user.getBonusAmount() + withdraw.getAmount());
    // userService.save(user);
    // }

    // withdraw.setStatus("REJECTED");
    // withdraw.setNote(reason);
    // withdraw.setProcessedAt(new Date());
    // withdrawRepository.save(withdraw);

    // logger.info("Withdrawal rejected - ID: {}, Reason: {}, User: {}",
    // id, reason, user != null ? user.getId() : "unknown");

    // } catch (Exception e) {
    // logger.error("Error rejecting withdrawal - ID: {}", id, e);
    // throw new WithdrawException("Lỗi khi từ chối yêu cầu rút tiền: " +
    // e.getMessage());
    // }
    // }

    public List<Withdraw> findApprovedByUserId(int userId) {
        return withdrawRepository.findByUserIdAndStatusIn(userId, Arrays.asList("APPROVED", "SUCCESS"));
    }
}
>>>>>>> Stashed changes
