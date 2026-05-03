package com.prep.controller;

import com.prep.dto.AttemptRequestDTO;
import com.prep.dto.AttemptResponseDTO;
import com.prep.service.AttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Attempt endpoints.
 *
 * Public / User endpoints:
 *   POST   /api/attempts              → submit a new attempt
 *   GET    /api/attempts/my           → get current user's attempts (JWT)
 *
 * Admin endpoints (requires ADMIN role / JWT):
 *   GET    /api/admin/attempts        → all attempts (used by attempts.html)
 *   GET    /api/admin/attempts/{id}   → single attempt
 *   DELETE /api/admin/attempts/{id}   → delete an attempt
 */
@RestController
@CrossOrigin(origins = "*")   // tighten this in production
public class AttemptController {

    @Autowired
    private AttemptService attemptService;

    /* ── ADMIN: GET ALL ──────────────────────── */
    @GetMapping("/api/admin/attempts")
    public ResponseEntity<List<AttemptResponseDTO>> getAllAttempts() {
        return ResponseEntity.ok(attemptService.getAllAttempts());
    }

    /* ── ADMIN: DELETE ───────────────────────── */
    @DeleteMapping("/api/admin/attempts/{id}")
    public ResponseEntity<Void> deleteAttempt(@PathVariable Long id) {
        attemptService.deleteAttempt(id);
        return ResponseEntity.noContent().build();
    }

    /* ── USER: SUBMIT ATTEMPT ────────────────── */
    /**
     * In production, extract userId from JWT token instead of a request param.
     * Example with Spring Security:
     *   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     *   Long userId = ((CustomUserDetails) auth.getPrincipal()).getId();
     *
     * For now using @RequestParam userId for simplicity.
     */
    @PostMapping("/api/attempts")
    public ResponseEntity<AttemptResponseDTO> submitAttempt(
            @RequestParam Long userId,
            @RequestBody AttemptRequestDTO request) {
        AttemptResponseDTO saved = attemptService.submitAttempt(userId, request);
        return ResponseEntity.ok(saved);
    }

    /* ── USER: MY ATTEMPTS ───────────────────── */
    @GetMapping("/api/attempts/my")
    public ResponseEntity<List<AttemptResponseDTO>> getMyAttempts(
            @RequestParam Long userId) {
        return ResponseEntity.ok(attemptService.getAttemptsByUser(userId));
    }

    /* ── ADMIN: BY USER ──────────────────────── */
    @GetMapping("/api/admin/attempts/user/{userId}")
    public ResponseEntity<List<AttemptResponseDTO>> getAttemptsByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(attemptService.getAttemptsByUser(userId));
    }
}