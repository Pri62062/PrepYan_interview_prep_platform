package com.prep.dto;

import java.time.LocalDateTime;

/**
 * Response DTO for Attempt entries.
 * Returned by GET /api/admin/attempts
 *
 * Field names match exactly what attempts.html expects:
 *   userId, questionId, isCorrect, score, timestamp
 */
public class AttemptResponseDTO {

    private Long          id;
    private Long          userId;
    private Long          questionId;
    private Boolean       isCorrect;
    private Integer       score;
    private LocalDateTime timestamp;

    // ── Getters ───────────────────────────────
    public Long          getId()           { return id; }
    public Long          getUserId()       { return userId; }
    public Long          getQuestionId()   { return questionId; }
    public Boolean       getIsCorrect()    { return isCorrect; }
    public Integer       getScore()        { return score; }
    public LocalDateTime getTimestamp()    { return timestamp; }

    // ── Setters ───────────────────────────────
    public void setId(Long id)                        { this.id = id; }
    public void setUserId(Long userId)                { this.userId = userId; }
    public void setQuestionId(Long questionId)        { this.questionId = questionId; }
    public void setIsCorrect(Boolean isCorrect)       { this.isCorrect = isCorrect; }
    public void setScore(Integer score)               { this.score = score; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
