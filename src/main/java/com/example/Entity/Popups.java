package com.example.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "popups")
public class Popups {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "content", columnDefinition = "TEXT")
	@Lob
	private String content;
	
	@Basic
	@Column(name = "is_active")
	private Boolean isActive = false;
	
	@Column(name = "position")
	private String position = "top-right";
	
	@Column(name = "duration")
	private Integer duration = 5;
	
	@Column(name = "animation")
	private String animation = "fade";
	
	@Column(name = "priority")
	private Integer priority = 0;
	
	@Column(name = "target_group")
	private String targetGroup;
	
	@Column(name = "schedule_start")
	private LocalDateTime scheduleStart;
	
	@Column(name = "schedule_end")
	private LocalDateTime scheduleEnd;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
	
	// Getter and Setter for isActive
	public Boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	// Alias methods for active property
	public Boolean getActive() {
		return isActive;
	}
	
	public void setActive(Boolean active) {
		this.isActive = active;
	}
}
