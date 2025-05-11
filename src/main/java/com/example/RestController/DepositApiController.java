package com.example.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Entity.Deposit;
import com.example.Service.DepositService;

@RestController
public class DepositApiController {

	@Autowired
	private DepositService depositService;
	
	 @GetMapping("/api/deposit/status/{transactionId}")
	public ResponseEntity<String> checkStatus(@PathVariable String transactionId){
		Deposit deposit = depositService.findByTransactionId(transactionId);

		String status = deposit.getStatus();
		return ResponseEntity.ok(status);
	}
}
