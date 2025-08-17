package com.captionGenerator.captainsGenerator.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CaptionRequestDto {

	private String picDescription;
	private String writingTone;
	private boolean addEmojis;
	private String mood;
	private String captionLength;
	private String language;
	private String platform;
	private MultipartFile picture;
	private Long userId;
}