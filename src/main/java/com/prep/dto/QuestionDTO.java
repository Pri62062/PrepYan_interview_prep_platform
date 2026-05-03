package com.prep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionDTO {

	private Long id;
	private String title;
	private String topic;
	private String difficulty;
	private String description;   // ✅ ADD
	private String answer;        // ✅ ADD
}