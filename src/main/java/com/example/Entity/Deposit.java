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
@Table(name = "deposit")
public class Deposit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "method")
	private String method;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "note", nullable = true, length = 500)
	private String note;
	
	@Column(name = "create_at")
	private Date createdAt;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
