package com.captionGenerator.captainsGenerator.Service;

import com.captionGenerator.captainsGenerator.Entity.Caption;
import com.captionGenerator.captainsGenerator.Repository.CaptionRepository;
import com.captionGenerator.captainsGenerator.dtos.CaptionRequestDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CaptionService {

	private final ChatClient chatClient;
	private final CaptionRepository captionRepository;

	public CaptionService(ChatClient.Builder chatClientBuilder, CaptionRepository captionRepository) {
		this.chatClient = chatClientBuilder.build();
		this.captionRepository = captionRepository;
	}

	@Transactional
	public Caption generateCaption(CaptionRequestDto requestDto) throws Exception {
		MultipartFile picture = requestDto.getPicture();
		if (picture == null || picture.isEmpty()) {
			throw new IllegalArgumentException("Image is required");
		}

		byte[] imageBytes = picture.getBytes();
		String contentType = picture.getContentType() != null ? picture.getContentType() : "image/jpeg";
		MimeType mimeType = MimeTypeUtils.parseMimeType(contentType);

		String details = buildDetails(requestDto);

		String captionText = chatClient
			.prompt()
			.system(s -> s.text("You are an expert social media caption writer. Write one concise caption only. If emojis are requested, add a few tasteful emojis. Avoid hashtags unless asked."))
			.user(u -> u
				.text("Generate a caption using the details below. Respond with only the caption text.\n{details}")
				.media(mimeType, new ByteArrayResource(imageBytes)))
			.param("details", details)
			.call()
			.content();

		Caption caption = Caption.builder()
			.text(captionText != null ? captionText.trim() : null)
			.picDescription(requestDto.getPicDescription())
			.writingTone(requestDto.getWritingTone())
			.addEmojis(requestDto.isAddEmojis())
			.mood(requestDto.getMood())
			.captionLength(requestDto.getCaptionLength())
			.language(requestDto.getLanguage())
			.platform(requestDto.getPlatform())
			.userId(requestDto.getUserId())
			.image(imageBytes)
			.build();

		return captionRepository.save(caption);
	}

	private String buildDetails(CaptionRequestDto dto) {
		StringBuilder sb = new StringBuilder();
		if (dto.getPicDescription() != null && !dto.getPicDescription().isBlank()) {
			sb.append("Description: ").append(dto.getPicDescription()).append('\n');
		}
		if (dto.getWritingTone() != null && !dto.getWritingTone().isBlank()) {
			sb.append("Tone: ").append(dto.getWritingTone()).append('\n');
		}
		sb.append("Emojis: ").append(dto.isAddEmojis() ? "yes" : "no").append('\n');
		if (dto.getMood() != null && !dto.getMood().isBlank()) {
			sb.append("Mood: ").append(dto.getMood()).append('\n');
		}
		if (dto.getCaptionLength() != null && !dto.getCaptionLength().isBlank()) {
			sb.append("Length: ").append(dto.getCaptionLength()).append('\n');
		}
		if (dto.getLanguage() != null && !dto.getLanguage().isBlank()) {
			sb.append("Language: ").append(dto.getLanguage()).append('\n');
		}
		if (dto.getPlatform() != null && !dto.getPlatform().isBlank()) {
			sb.append("Platform: ").append(dto.getPlatform()).append('\n');
		}
		return sb.toString();
	}
}