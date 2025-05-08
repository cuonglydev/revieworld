package com.example.Entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "users")
public class User {

	public enum Rank {
		BRONZE, SILVER, GOLD, PLATINUM, DIAMOND
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "provider")
	private String provider;
	
	@Column(name = "provider_id")
	private String providerId;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "picture", nullable = true)
	private String picture;
	
	@Column(name = "amount")
	private Double amount = 0.0;
	
	@Column(name = "bonus_amount")
	private Double bonusAmount = 0.0;
	
	@Column(name = "total_deposited")
	private Double totalDeposited;
	
	@Column(name = "invite_code")
	private String inviteCode;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "token")
	private String token;
	
	@Enumerated(EnumType.STRING)
	private Rank rank;
	
	@OneToOne(mappedBy = "user")
	private UserAffiliate userAffiliate;
	
	@OneToMany(mappedBy = "user")
	private List<Mission> missions;
	
	@OneToMany(mappedBy = "user")
	private List<Withdraw> withdraws;
	
	@OneToMany(mappedBy = "user")
	private List<Deposit> deposits;
	
	@OneToMany(mappedBy = "user")
	private List<Order> orders;

	@OneToMany(mappedBy = "user")
	private List<UserBank> userBanks;
}
