package com.example.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "affiliate")
public class Affiliate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "status")
	private Boolean status = true;

	// Tỷ lệ hoa hồng cơ bản cho người giới thiệu
	@Column(name = "base_commission_percentage")
	private Double baseCommissionPercentage = 5.0;

	// Tỷ lệ hoa hồng cho người dùng mới
	@Column(name = "new_user_commission_percentage")
	private Double newUserCommissionPercentage = 5.0;

	// Tỷ lệ hoa hồng cho người được giới thiệu
	@Column(name = "referral_commission_percentage") 
	private Double referralCommissionPercentage = 2.0;

	// Số tiền tối thiểu để nhận hoa hồng
	@Column(name = "min_commission_amount")
	private Double minCommissionAmount = 10.0;

	// Số tiền hoa hồng tối đa cho mỗi giao dịch
	@Column(name = "max_commission_amount")
	private Double maxCommissionAmount = 1000.0;

	// Thời gian chờ để nhận hoa hồng (tính bằng ngày)
	@Column(name = "commission_waiting_days")
	private Integer commissionWaitingDays = 0;

	// Mô tả về chương trình affiliate
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	// Ngày bắt đầu chương trình
	@Column(name = "start_date")
	private java.util.Date startDate;

	// Ngày kết thúc chương trình (null nếu không có)
	@Column(name = "end_date")
	private java.util.Date endDate;
}
