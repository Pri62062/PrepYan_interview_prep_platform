package com.prep.controller;

import com.prep.repository.AiLogRepository;
import com.prep.repository.AttemptRepository;
import com.prep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * GET /api/admin/stats
 * Returns the four numbers shown on the Dashboard stat cards.
 *
 * Response shape (matches dashboard.html):
 * {
 *   "totalUsers":     120,
 *   "totalQuestions":  80,
 *   "totalAttempts":  500,
 *   "aiUsage":        320
 * }
 *
 * NOTE: inject your existing QuestionRepository too.
 */
@RestController
@CrossOrigin(origins = "*")
public class AdminStatsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttemptRepository attemptRepository;

    @Autowired
    private AiLogRepository aiLogRepository;

    /*
     * Inject your QuestionRepository here.
     * Uncomment once you have it:
     *
     * @Autowired
     * private QuestionRepository questionRepository;
     */

    @GetMapping("/api/admin/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("totalUsers",     userRepository.count());
        stats.put("totalAttempts",  attemptRepository.count());
        stats.put("aiUsage",        aiLogRepository.count());

        // Uncomment when QuestionRepository is available:
        // stats.put("totalQuestions", questionRepository.count());
        stats.put("totalQuestions", 0L); // placeholder

        return ResponseEntity.ok(stats);
    }
}