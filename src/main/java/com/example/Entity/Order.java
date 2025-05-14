package com.example.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", nullable = false, length = 500)
	private String name;
	
	@Column(name = "url", nullable = false)
	private String url;
	
	@Column(name = "photo", length = 500)
	private String photo;
	
	@Column(name = "description", nullable = true)
	private String description;
	
	@Column(name = "language", nullable = true)
	private String language;
	
	@Column(name = "slug", nullable = false, length = 500)
	private String slug;
	
	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "item_amount")
	private Double itemAmount;
	
	@Column(name = "quantity")
	private int quantity;
	
	@Column(name = "quantity_done")
	private int quantityDone;
	
	@Column(name = "turn_quantity")
	private int turnQuantity;
	
	@Column(name = "turn_quantity_done")
	private int turnQuantityDone;
	
	@Column(name = "last_turn_time")
	private Date lastTurnTime;
	
	@Column(name = "detail", nullable = true, columnDefinition = "TEXT")
	@Lob
	private String detail;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "order_type_id")
	private OrderType orderType;
	
}
