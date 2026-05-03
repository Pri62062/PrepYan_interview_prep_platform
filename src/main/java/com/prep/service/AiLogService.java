package com.prep.service;

import com.prep.dto.AiLogRequestDTO;
import com.prep.dto.AiLogResponseDTO;
import com.prep.entity.AiLog;
import com.prep.repository.AiLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiLogService {

    @Autowired
    private AiLogRepository aiLogRepository;

    // ── GET ALL (admin view) ───────────────────
    public List<AiLogResponseDTO> getAllLogs() {
        return aiLogRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── GET BY USER ────────────────────────────
    public List<AiLogResponseDTO> getLogsByUser(Long userId) {
        return aiLogRepository.findByUserId(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── SAVE NEW SESSION ───────────────────────
    /**
     * Called by the AI Tutor controller after every interaction.
     * userId comes from the JWT token — never from the request body.
     */
    public AiLogResponseDTO saveLog(Long userId, AiLogRequestDTO req) {
        AiLog log = new AiLog();
        log.setUserId(userId);
        log.setPrompt(req.getPrompt());
        log.setResponse(req.getResponse());
        log.setTokens(req.getTokens());
        log.setStatus(req.getStatus() != null ? req.getStatus() : "completed");
        // timestamp set by @PrePersist

        AiLog saved = aiLogRepository.save(log);
        return toDTO(saved);
    }

    // ── DELETE LOG ─────────────────────────────
    public void deleteLog(Long id) {
        if (!aiLogRepository.existsById(id)) {
            throw new RuntimeException("AI log not found with id: " + id);
        }
        aiLogRepository.deleteById(id);
    }

    // ── LAST 7 DAYS ────────────────────────────
    public List<AiLogResponseDTO> getLastSevenDays() {
        LocalDateTime since = LocalDateTime.now().minusDays(7);
        return aiLogRepository.findByTimestampAfter(since)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ── ENTITY → DTO ───────────────────────────
    private AiLogResponseDTO toDTO(AiLog l) {
        AiLogResponseDTO dto = new AiLogResponseDTO();
        dto.setId(l.getId());
        dto.setUserId(l.getUserId());
        dto.setPrompt(l.getPrompt());
        dto.setResponse(l.getResponse());
        dto.setTokens(l.getTokens());
        dto.setStatus(l.getStatus());
        dto.setTimestamp(l.getTimestamp());
        return dto;
    }
}