package com.prep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prep.service.AIService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AIController {

    @Autowired
    private AIService service;

    // ✅ Answer Feedback (can stay dummy or later AI)
    @PostMapping("/feedback")
    public String feedback(@RequestBody String answer) {
        return service.getFeedback(answer);
    }

    // ✅ Explanation (can stay dummy or later AI)
    @PostMapping("/explain")
    public String explain(@RequestBody String question) {
        return service.explain(question);
    }

    // 🔥 MAIN AI ENDPOINT (IMPORTANT)
    @PostMapping("/ask")
    public String ask(@RequestBody String question) {
        return service.askAI(question);   // ✅ REAL AI CALL
    }

    // 🔥 Code Review (AI based)
    @PostMapping("/code-review")
    public String review(@RequestBody String code) {
        return service.reviewCode(code);
    }
} 