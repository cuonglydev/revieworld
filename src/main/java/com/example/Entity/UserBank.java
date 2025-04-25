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
@Table(name = "user_bank")
public class UserBank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "method")
	private String method;
	
	@Column(name = "bank_name")
	private String bankName;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "account_name")
	private String accountName;

	@Column(name = "created_at")
	private Date createdAt;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
