package com.prep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.prep.service.AIService;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService service;

    // ───────── ASK AI ─────────
    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody String question) {
        try {
            return ResponseEntity.ok(service.askAI(question));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(500)
                    .body("AI ERROR (ASK): " + e.getMessage());
        }
    }

    // ───────── EXPLAIN ─────────
    @PostMapping("/explain")
    public ResponseEntity<?> explain(@RequestBody String topic) {
        try {
            return ResponseEntity.ok(service.explainConcept(topic));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(500)
                    .body("AI ERROR (EXPLAIN): " + e.getMessage());
        }
    }

    // ───────── CODE REVIEW ─────────
    @PostMapping("/code-review")
    public ResponseEntity<?> review(@RequestBody String code) {
        try {
            return ResponseEntity.ok(service.reviewCode(code));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(500)
                    .body("AI ERROR (REVIEW): " + e.getMessage());
        }
    }

    // ───────── ANSWER FEEDBACK ─────────
    @PostMapping("/feedback")
    public ResponseEntity<?> feedback(
            @RequestParam String question,
            @RequestBody String answer) {

        try {
            return ResponseEntity.ok(service.evaluateAnswer(question, answer));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(500)
                    .body("AI ERROR (FEEDBACK): " + e.getMessage());
        }
    }
}