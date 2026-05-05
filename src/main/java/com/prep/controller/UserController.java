package com.prep.controller;

import com.prep.dto.QuestionDTO;
import com.prep.entity.Question;
import com.prep.service.QuestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")

public class UserController {

    @Autowired
    private QuestionService service;

    // ✅ SAFE GET
    @GetMapping("/questions")
    public List<QuestionDTO> getAll() {
        return service.getAllDTO();
    }

    // ✅ SAFE FILTER
    @GetMapping("/filter")
    public List<QuestionDTO> filter(
            @RequestParam String topic,
            @RequestParam String difficulty) {

        return service.getByTopicAndDifficulty(topic, difficulty)
                .stream()
                .map(q ->  new QuestionDTO(
                	    q.getId(),
                	    q.getTitle(),
                	    q.getTopic(),
                	    q.getDifficulty(),
                	    q.getDescription(),
                	    q.getAnswer()
                	))
                .toList();
    }
}