package com.prep.controller;

import com.prep.dto.AiLogRequestDTO;
import com.prep.dto.AiLogResponseDTO;
import com.prep.service.AiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI Log endpoints.
 *
 * Internal / AI Tutor endpoint:
 *   POST   /api/ai/log                → save a new AI session after every chat
 *
 * Admin endpoints (requires ADMIN role):
 *   GET    /api/admin/ai-logs         → all logs (used by ai-logs.html)
 *   GET    /api/admin/ai-logs/{id}    → single log
 *   DELETE /api/admin/ai-logs/{id}    → delete a log
 *
 * User endpoint:
 *   GET    /api/ai/logs/my            → current user's own AI logs
 */
@RestController
@CrossOrigin(origins = "*")   // tighten this in production
public class AiLogController {

    @Autowired
    private AiLogService aiLogService;

    /* ── ADMIN: GET ALL ──────────────────────── */
    @GetMapping("/api/admin/ai-logs")
    public ResponseEntity<List<AiLogResponseDTO>> getAllLogs() {
        return ResponseEntity.ok(aiLogService.getAllLogs());
    }

    /* ── ADMIN: DELETE ───────────────────────── */
    @DeleteMapping("/api/admin/ai-logs/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        aiLogService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }

    /* ── INTERNAL: SAVE AI SESSION ───────────── */
    /**
     * Call this inside your AI Tutor service after every chat response.
     * Pass userId from the authenticated JWT — never trust it from the body.
     *
     * Example usage in AiTutorController:
     *   aiLogService.saveLog(userId, new AiLogRequestDTO(prompt, response, tokens, "completed"));
     *
     * Or expose as an internal REST endpoint (only called server-side):
     */
    @PostMapping("/api/ai/log")
    public ResponseEntity<AiLogResponseDTO> saveLog(
            @RequestParam Long userId,
            @RequestBody AiLogRequestDTO request) {
        AiLogResponseDTO saved = aiLogService.saveLog(userId, request);
        return ResponseEntity.ok(saved);
    }

    /* ── USER: MY LOGS ───────────────────────── */
    @GetMapping("/api/ai/logs/my")
    public ResponseEntity<List<AiLogResponseDTO>> getMyLogs(
            @RequestParam Long userId) {
        return ResponseEntity.ok(aiLogService.getLogsByUser(userId));
    }

    /* ── ADMIN: LAST 7 DAYS ──────────────────── */
    @GetMapping("/api/admin/ai-logs/recent")
    public ResponseEntity<List<AiLogResponseDTO>> getRecentLogs() {
        return ResponseEntity.ok(aiLogService.getLastSevenDays());
    }
}
