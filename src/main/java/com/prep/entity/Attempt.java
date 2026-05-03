package com.prep.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Stores every question attempt made by a user.
 * Maps to: GET /api/admin/attempts
 */
@Entity
@Table(name = "attempts")
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key — user who attempted
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Foreign key — question attempted
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    // true = correct, false = incorrect, null = not evaluated yet
    @Column(name = "is_correct")
    private Boolean isCorrect;

    // Optional numeric score (e.g. 0–10)
    @Column(name = "score")
    private Integer score;

    // When the attempt was submitted
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) this.timestamp = LocalDateTime.now();
    }

    // ── Getters & Setters ──────────────────────

    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }

    public Long getUserId()                { return userId; }
    public void setUserId(Long userId)     { this.userId = userId; }

    public Long getQuestionId()            { return questionId; }
    public void setQuestionId(Long qId)    { this.questionId = qId; }

    public Boolean getIsCorrect()          { return isCorrect; }
    public void setIsCorrect(Boolean v)    { this.isCorrect = v; }

    public Integer getScore()              { return score; }
    public void setScore(Integer score)    { this.score = score; }

    public LocalDateTime getTimestamp()    { return timestamp; }
    public void setTimestamp(LocalDateTime t) { this.timestamp = t; }
}