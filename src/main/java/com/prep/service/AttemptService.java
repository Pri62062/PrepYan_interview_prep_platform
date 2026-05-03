package com.prep.service;

import com.prep.dto.AttemptRequestDTO;
import com.prep.dto.AttemptResponseDTO;
import com.prep.entity.Attempt;
import com.prep.repository.AttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttemptService {

    @Autowired
    private AttemptRepository attemptRepository;

    // ── GET ALL (admin view) ───────────────────
    public List<AttemptResponseDTO> getAllAttempts() {
        return attemptRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── GET BY USER ────────────────────────────
    public List<AttemptResponseDTO> getAttemptsByUser(Long userId) {
        return attemptRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── SUBMIT ATTEMPT ─────────────────────────
    /**
     * Called when a user submits an answer.
     * The controller passes userId from the JWT token,
     * so users cannot fake their own userId.
     */
    public AttemptResponseDTO submitAttempt(Long userId, AttemptRequestDTO req) {
        Attempt attempt = new Attempt();
        attempt.setUserId(userId);
        attempt.setQuestionId(req.getQuestionId());
        attempt.setIsCorrect(req.getIsCorrect());
        attempt.setScore(req.getScore());
        // timestamp is set by @PrePersist

        Attempt saved = attemptRepository.save(attempt);
        return toDTO(saved);
    }

    // ── DELETE ─────────────────────────────────
    public void deleteAttempt(Long id) {
        if (!attemptRepository.existsById(id)) {
            throw new RuntimeException("Attempt not found with id: " + id);
        }
        attemptRepository.deleteById(id);
    }

    // ── ENTITY → DTO ───────────────────────────
    private AttemptResponseDTO toDTO(Attempt a) {
        AttemptResponseDTO dto = new AttemptResponseDTO();
        dto.setId(a.getId());
        dto.setUserId(a.getUserId());
        dto.setQuestionId(a.getQuestionId());
        dto.setIsCorrect(a.getIsCorrect());
        dto.setScore(a.getScore());
        dto.setTimestamp(a.getTimestamp());
        return dto;
    }
}
