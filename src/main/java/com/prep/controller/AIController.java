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

    // 🔥 MAIN ASK
    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody String question) {
        try {
            return ResponseEntity.ok(service.askAI(question));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("⚠️ AI service temporarily unavailable");
        }
    }

    // 🔥 EXPLAIN
    @PostMapping("/explain")
    public ResponseEntity<String> explain(@RequestBody String topic) {
        try {
            return ResponseEntity.ok(service.explainConcept(topic));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error explaining topic");
        }
    }

    // 🔥 CODE REVIEW
    @PostMapping("/code-review")
    public ResponseEntity<String> review(@RequestBody String code) {
        try {
            return ResponseEntity.ok(service.reviewCode(code));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error reviewing code");
        }
    }

    // 🔥 ANSWER FEEDBACK (UPDATED)
    @PostMapping("/feedback")
    public ResponseEntity<String> feedback(
            @RequestParam String question,
            @RequestBody String answer) {

        try {
            return ResponseEntity.ok(service.evaluateAnswer(question, answer));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error evaluating answer");
        }
    }
}