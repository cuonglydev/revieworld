package com.example.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
@Table(name = "order_type")
public class OrderType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "fee_percentage")
	private Double feePercentage;

	@Column(name = "photo")
	private String photo;

	@Column(name = "price", nullable = false)
	private Double price;

	@Column(name = "promotional_price")
	private Double promotionalPrice;

	@Column(name = "slug", nullable = false, length = 500)
	private String slug;

	@Column(name = "url")
	private String url;

	@Column(name = "guide", columnDefinition = "TEXT", nullable = true)
	@Lob
	private String guide;

	@Column(name = "created_at")
	private Date createdAt;
}
