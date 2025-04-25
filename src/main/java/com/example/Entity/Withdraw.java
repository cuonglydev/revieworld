package com.example.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "withdraw")
public class Withdraw {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "note", nullable = true, length = 500)
	private String note;
	
	@Column(name = "user_bank_method")
	private String userBankMethod;
	
	@Column(name = "bank_name")
	private String bankName;
	
	@Column(name = "account_name")
	private String accountName;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "created_at")
	private Date createAt;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
