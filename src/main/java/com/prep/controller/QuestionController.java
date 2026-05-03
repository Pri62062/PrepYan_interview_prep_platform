package com.prep.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prep.dto.QuestionDTO;
import com.prep.entity.Question;
import com.prep.service.QuestionService;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService service;

    // ✅ GET ALL (SAFE - DTO)
    @GetMapping
    public List<QuestionDTO> getAll() {
        return service.getAllDTO();
    }

    // ✅ GET BY ID (FULL DATA)
    @GetMapping("/{id}")
    public Question getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ✅ ADD QUESTION
    @PostMapping
    public Question add(@RequestBody Question q) {
        return service.save(q);
    }

    // ✅ UPDATE QUESTION
    @PutMapping("/{id}")
    public Question update(@PathVariable Long id, @RequestBody Question q) {
        return service.update(id, q);
    }

    // ✅ DELETE QUESTION
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Question deleted successfully";
    }

    // ✅ SAFE FILTER (FINAL FIX)
    @GetMapping("/filter")
    public List<QuestionDTO> filter(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficulty) {

        List<Question> list;

        if (topic != null && difficulty != null) {
            list = service.getByTopicAndDifficulty(topic, difficulty);
        } else if (topic != null) {
            list = service.getByTopic(topic);
        } else if (difficulty != null) {
            list = service.getByDifficulty(difficulty);
        } else {
            list = service.getLimited(10);   // ✅ FIXED (NO MORE getAll())
        }

        return list.stream()
                .map(q ->new QuestionDTO(
                	    q.getId(),
                	    q.getTitle(),
                	    q.getTopic(),
                	    q.getDifficulty(),
                	    q.getDescription(),   // ✅ ADD
                	    q.getAnswer()         // ✅ ADD
                	))
                .toList();
    }
}