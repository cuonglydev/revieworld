package com.example.Entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

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
	@OneToMany(mappedBy = "orderType", cascade = CascadeType.ALL)
	private List<Language> languages;
	@Column(name = "name")
	private String name;

	@Column(name = "fee_percentage")
	private Double feePercentage;
	@Column(name = "photo")
	private String photo;

	@Column(name = "slug", nullable = false, length = 500)
	private String slug;

	@Column(name = "url")
	private String url;

	@Column(name = "link")
	private String link;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "created_at")
	private Date createdAt;
}
