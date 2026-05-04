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

    // 🔥 HEALTH CHECK (IMPORTANT for debugging)
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("TEST OK");
    }

    // ✅ GET ALL (SAFE)
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            System.out.println("📦 GET ALL QUESTIONS");
            return ResponseEntity.ok(service.getAllDTO());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching questions");
        }
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Question not found");
        }
    }

    // ✅ ADD
    @PostMapping
    public ResponseEntity<?> add(@RequestBody Question q) {
        try {
            return ResponseEntity.ok(service.save(q));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving question");
        }
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Question q) {
        try {
            return ResponseEntity.ok(service.update(id, q));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating question");
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting question");
        }
    }

    // ✅ FILTER (SAFE + LIMITED)
    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficulty) {

        try {
            List<Question> list;

            if (topic != null && difficulty != null) {
                list = service.getByTopicAndDifficulty(topic, difficulty);
            } else if (topic != null) {
                list = service.getByTopic(topic);
            } else if (difficulty != null) {
                list = service.getByDifficulty(difficulty);
            } else {
                list = service.getLimited(5); // 🔥 reduce load
            }

            List<QuestionDTO> result = list.stream()
                    .map(q -> new QuestionDTO(
                            q.getId(),
                            q.getTitle(),
                            q.getTopic(),
                            q.getDifficulty(),
                            q.getDescription(),
                            q.getAnswer()
                    ))
                    .toList();

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error filtering questions");
        }
    }
}