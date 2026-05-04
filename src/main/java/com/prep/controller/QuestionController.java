package com.prep.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prep.dto.QuestionDTO;
import com.prep.entity.Question;
import com.prep.service.QuestionService;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionService service;

    // 🔥 HEALTH CHECK (VERY IMPORTANT)
    @GetMapping("/test")
    public String test() {
        return "TEST OK";
    }

    // ✅ GET ALL (LIGHTWEIGHT)
    @GetMapping
    public List<QuestionDTO> getAll() {
        System.out.println("📦 FETCH QUESTIONS");
        return service.getAllDTO();
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public Question getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // ✅ ADD
    @PostMapping
    public Question add(@RequestBody Question q) {
        return service.save(q);
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public Question update(@PathVariable Long id, @RequestBody Question q) {
        return service.update(id, q);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Deleted";
    }

    // ✅ FILTER
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
            list = service.getLimited(5); // 🔥 LIMIT
        }

        return list.stream()
                .map(q -> new QuestionDTO(
                        q.getId(),
                        q.getTitle(),
                        q.getTopic(),
                        q.getDifficulty(),
                        null,
                        null
                ))
                .toList();
    }
}