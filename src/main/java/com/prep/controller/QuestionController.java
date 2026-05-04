package com.prep.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAll() {

        System.out.println("📦 GET ALL QUESTIONS HIT");

        return ResponseEntity.ok(service.getAllDTO());
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Question> getById(@PathVariable Long id) {

        System.out.println("🔍 GET QUESTION BY ID: " + id);

        return ResponseEntity.ok(service.getById(id));
    }

    // ✅ ADD
    @PostMapping
    public ResponseEntity<Question> add(@RequestBody Question q) {
        return ResponseEntity.ok(service.save(q));
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Question> update(@PathVariable Long id, @RequestBody Question q) {
        return ResponseEntity.ok(service.update(id, q));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted");
    }

    // ✅ FILTER
    @GetMapping("/filter")
    public ResponseEntity<List<QuestionDTO>> filter(
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
            list = service.getLimited(10);
        }

        List<QuestionDTO> result = list.stream()
                .map(q -> new QuestionDTO(
                        q.getId(),
                        q.getTitle(),
                        q.getTopic(),
                        q.getDifficulty(),
                        null,
                        null
                ))
                .toList();

        return ResponseEntity.ok(result);
    }
}