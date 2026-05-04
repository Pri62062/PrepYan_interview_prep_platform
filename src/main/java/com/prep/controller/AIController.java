package com.prep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.prep.service.AIService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    private AIService service;

    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody String question) {

        System.out.println("🤖 AI ASK HIT");

        try {
            String res = service.askAI(question);
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("AI service temporarily unavailable");
        }
    }

    @PostMapping("/feedback")
    public ResponseEntity<String> feedback(@RequestBody String answer) {
        return ResponseEntity.ok(service.getFeedback(answer));
    }

    @PostMapping("/explain")
    public ResponseEntity<String> explain(@RequestBody String question) {
        return ResponseEntity.ok(service.explain(question));
    }

    @PostMapping("/code-review")
    public ResponseEntity<String> review(@RequestBody String code) {
        return ResponseEntity.ok(service.reviewCode(code));
    }
}