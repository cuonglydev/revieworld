package com.example.Entity;

import java.util.Date;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "user_bank")
public class UserBank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank(message = "Phương thức không được để trống")
	@Pattern(regexp = "^(BANK|USDT)$", message = "Phương thức không hợp lệ")
	@Column(name = "method", nullable = false, length = 10)
	private String method;
	
	@Column(name = "bank_name")
	private String bankName;
	
	@NotBlank(message = "Số tài khoản/địa chỉ ví không được để trống")
	@Column(name = "account_number", nullable = false)
	private String accountNumber;
	
	@Column(name = "account_name")
	private String accountName;

	@CreationTimestamp 
	@Column(name = "created_at")
	private Date createdAt;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method != null ? method.trim().toUpperCase() : null;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName != null ? bankName.trim() : null;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber != null ? accountNumber.trim() : null;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName != null ? accountName.trim() : null;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// Helper methods
	public void prepareForSave(User currentUser) {
		this.setUser(currentUser);
		
		// Trim all string fields
		if (this.method != null) this.method = this.method.trim().toUpperCase();
		if (this.bankName != null) this.bankName = this.bankName.trim();
		if (this.accountNumber != null) this.accountNumber = this.accountNumber.trim();
		if (this.accountName != null) this.accountName = this.accountName.trim();
		
		// Set null values for USDT method
		if ("USDT".equals(this.method)) {
			this.bankName = null;
			this.accountName = null;
		}
	}

	public void validate() {
		if (this.method == null || this.method.trim().isEmpty()) {
			throw new IllegalArgumentException("Phương thức không được để trống");
		}
		if (!this.method.matches("^(BANK|USDT)$")) {
			throw new IllegalArgumentException("Phương thức không hợp lệ");
		}
		if (this.accountNumber == null || this.accountNumber.trim().isEmpty()) {
			throw new IllegalArgumentException("Số tài khoản/địa chỉ ví không được để trống");
		}
		if ("BANK".equals(this.method)) {
			if (this.bankName == null || this.bankName.trim().isEmpty()) {
				throw new IllegalArgumentException("Tên ngân hàng không được để trống");
			}
			if (this.accountName == null || this.accountName.trim().isEmpty()) {
				throw new IllegalArgumentException("Tên tài khoản không được để trống");
			}
		}
	}

	@Override
	public String toString() {
		return "UserBank{" +
				"id=" + id +
				", method='" + method + '\'' +
				", bankName='" + bankName + '\'' +
				", accountNumber='" + accountNumber + '\'' +
				", accountName='" + accountName + '\'' +
				", createdAt=" + createdAt +
				'}';
	}
}
