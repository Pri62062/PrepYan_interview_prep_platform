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

    // ✅ Health check
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Questions API OK ✅");
    }

    // ✅ GET ALL (safe DTO + no heavy fields)
    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAll() {
        return ResponseEntity.ok(service.getAllDTO());
    }

    // ✅ GET BY ID (SAFE + DTO + no crash)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        QuestionDTO dto = service.getByIdDTO(id);

        if (dto == null) {
            return ResponseEntity.status(404).body("Question not found");
        }

        return ResponseEntity.ok(dto);
    }

    // ✅ ADD
    @PostMapping
    public ResponseEntity<Question> add(@RequestBody Question q) {
        return ResponseEntity.ok(service.save(q));
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody Question q) {

        Question updated = service.update(id, q);

        if (updated == null) {
            return ResponseEntity.status(404).body("Question not found");
        }

        return ResponseEntity.ok(updated);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // ✅ FILTER (already safe)
    @GetMapping("/filter")
    public ResponseEntity<List<QuestionDTO>> filter(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficulty) {

        List<QuestionDTO> result;

        if (topic != null && difficulty != null) {
            result = service.getByTopicAndDifficulty(topic, difficulty);
        } else if (topic != null) {
            result = service.getByTopic(topic);
        } else if (difficulty != null) {
            result = service.getByDifficulty(difficulty);
        } else {
            result = service.getLimited(20);
        }

        return ResponseEntity.ok(result);
    }
}