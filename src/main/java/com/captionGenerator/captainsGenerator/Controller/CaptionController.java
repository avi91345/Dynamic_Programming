package com.captionGenerator.captainsGenerator.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.captionGenerator.captainsGenerator.Entity.Caption;
import com.captionGenerator.captainsGenerator.dtos.CaptionRequestDto;
import com.captionGenerator.captainsGenerator.Service.CaptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/captions")
public class CaptionController {

	@Autowired
	private CaptionService captionService;

	@PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Caption> generateCaption(@ModelAttribute CaptionRequestDto requestDto) throws Exception {
		Caption caption = captionService.generateCaption(requestDto);
		return ResponseEntity.ok(caption);
	}
}