package com.example.Controller.Admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.Entity.User;
import com.example.Entity.Withdraw;
import com.example.Service.UserService;
import com.example.Service.WithdrawService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/admin")
public class WithdrawManageController {

    @Autowired
    private WithdrawService withdrawService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/withdraw/approve/{id}")
    public String getMethodName(@PathVariable int id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    	String referer = request.getHeader("Referer");
    	try {
    		Withdraw withdraw = withdrawService.findById(id);
    		
    		 if(!"WAITING".equals(withdraw.getStatus())) {
    			 redirectAttributes.addFlashAttribute("danger", "Đơn này không trong trạng thái chờ!");
    			 return "redirect:" + (referer != null ? referer : "/");
    		 }
    		 
    		 withdraw.setStatus("APPROVED");
    		 withdraw.setProcessedAt(new Date());
    		 withdrawService.save(withdraw);
    		 
    		 redirectAttributes.addFlashAttribute("success", "Dã duyệt đơn!");
    	}catch (Exception e) {
    		redirectAttributes.addFlashAttribute("danger", "Duyệt đơn thất bại!");
    		// TODO: handle exception
		}
        return "redirect:" + (referer != null ? referer : "/");
    
    }
    
    @PostMapping("/withdraw/refuse")
    public String postMethodName(@RequestParam("id") int id ,@RequestParam("note") String note, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        //TODO: process POST request
    	String referer = request.getHeader("Referer");
    	try {
    	  Withdraw withdraw = withdrawService.findById(id);
    	  
    	  if(!"WAITING".equals(withdraw.getStatus())) {
 			 redirectAttributes.addFlashAttribute("danger", "Đơn này không trong trạng thái chờ!");
 			 return "redirect:" + (referer != null ? referer : "/");
 		 }
    	  
    	  withdraw.setStatus("REFUSED");
    	  withdraw.setProcessedAt(new Date());
    	  withdrawService.save(withdraw);
    	  
    	  User user = userService.findById(withdraw.getUser().getId());
    	  user.setBonusAmount(user.getBonusAmount() + withdraw.getAmount());
    	  userService.save(user);
    	  
    	  redirectAttributes.addFlashAttribute("success", "Từ chối rút tiền thành công!");
      }catch (Exception e) {
		// TODO: handle exception
    	  redirectAttributes.addFlashAttribute("danger", "Từ chối rút tiền thất bại!");
	}
      return "redirect:" + (referer != null ? referer : "/");
    }
    
    

//    @PostMapping("/withdraw/{id}/approve")
//    @ResponseBody
//    public ResponseEntity<?> approveRequest(@PathVariable int id) {
//        withdrawService.approveWithdrawal(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/withdraw/{id}/reject")
//    @ResponseBody
//    public ResponseEntity<?> rejectRequest(@PathVariable int id, @RequestParam String reason) {
//        withdrawService.rejectWithdrawal(id, reason);
//        return ResponseEntity.ok().build();
//    }
} 