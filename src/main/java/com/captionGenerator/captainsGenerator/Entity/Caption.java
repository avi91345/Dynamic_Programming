package com.captionGenerator.captainsGenerator.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "captions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 500)
	private String text;

	@Column(length = 500)
	private String picDescription;

	@Column(length = 50)
	private String writingTone;

	private boolean addEmojis;

	@Column(length = 50)
	private String mood;

	@Column(length = 20)
	private String captionLength;

	@Column(length = 50)
	private String language;

	@Column(length = 50)
	private String platform;

	@Column(nullable = false)
	private Long userId;

	@Lob
	@Column(columnDefinition = "BLOB")
	private byte[] image;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}