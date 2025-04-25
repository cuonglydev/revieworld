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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mission")
public class Mission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "url", length = 500)
	private String url;
	
	@Column(name = "note", nullable = true, length = 500)
	private String note;
	
	@Column(name = "admin_note", nullable = true, length = 500)
	private String adminNote;
	
	@Column(name = "amount")
	private Double Amount;
	
	@Column(name = "status_date")
	private Date statusDate;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order Order;
}
