package com.prep.dto;

/**
 * Payload sent by frontend when submitting a question attempt.
 * POST /api/attempts
 */
public class AttemptRequestDTO {

    private Long    questionId;
    private Boolean isCorrect;
    private Integer score;       // optional, 0–10

    public Long    getQuestionId()              { return questionId; }
    public void    setQuestionId(Long qId)      { this.questionId = qId; }

    public Boolean getIsCorrect()               { return isCorrect; }
    public void    setIsCorrect(Boolean v)      { this.isCorrect = v; }

    public Integer getScore()                   { return score; }
    public void    setScore(Integer score)      { this.score = score; }
}
