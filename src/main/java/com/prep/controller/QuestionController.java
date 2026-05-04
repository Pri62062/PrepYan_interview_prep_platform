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

    // ── Health check ──────────────────────────
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Questions API OK ✅");
    }

    // ── GET ALL (paginated — memory safe) ─────
    // Returns List<QuestionDTO> — no answer field exposed
    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAll() {
        System.out.println("📦 FETCH ALL QUESTIONS");
        return ResponseEntity.ok(service.getAllDTO());
    }

    // ── GET BY ID (returns full Question with answer) ──
    @GetMapping("/{id}")
    public ResponseEntity<Question> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ── ADD (admin) ────────────────────────────
    @PostMapping
    public ResponseEntity<Question> add(@RequestBody Question q) {
        return ResponseEntity.ok(service.save(q));
    }

    // ── UPDATE (admin) ─────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<Question> update(
            @PathVariable Long id,
            @RequestBody Question q) {
        return ResponseEntity.ok(service.update(id, q));
    }

    // ── DELETE (admin) ─────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // ── FILTER by topic / difficulty ───────────
    // FIX 1: Removed __ __ formatting around method calls
    // FIX 2: Service now returns List<QuestionDTO> directly — no manual mapping needed
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
            // No filter given — return limited set (memory safe)
            result = service.getLimited(20);
        }

        return ResponseEntity.ok(result);
    }
}