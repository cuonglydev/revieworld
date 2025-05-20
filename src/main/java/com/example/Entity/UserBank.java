package com.example.Entity;

import java.util.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_bank")
public class UserBank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "method", nullable = false)
	private String method;
	
	@Column(name = "bank_name")
	private String bankName;
	
	@Column(name = "account_number", nullable = false)
	private String accountNumber;
	
	@Column(name = "account_name")
	private String accountName;

	@CreationTimestamp 
	@Column(name = "created_at")
	private Date createdAt;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
