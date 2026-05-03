package com.prep.dto;

import java.time.LocalDateTime;

/**
 * Response DTO for AI Log entries.
 * Returned by GET /api/admin/ai-logs
 *
 * Field names match exactly what ai-logs.html expects:
 *   userId, prompt, response, tokens, status, timestamp
 */
public class AiLogResponseDTO {

    private Long          id;
    private Long          userId;
    private String        prompt;
    private String        response;
    private Integer       tokens;
    private String        status;
    private LocalDateTime timestamp;

    // ── Getters ───────────────────────────────
    public Long          getId()          { return id; }
    public Long          getUserId()      { return userId; }
    public String        getPrompt()      { return prompt; }
    public String        getResponse()    { return response; }
    public Integer       getTokens()      { return tokens; }
    public String        getStatus()      { return status; }
    public LocalDateTime getTimestamp()   { return timestamp; }

    // ── Setters ───────────────────────────────
    public void setId(Long id)                       { this.id = id; }
    public void setUserId(Long userId)               { this.userId = userId; }
    public void setPrompt(String prompt)             { this.prompt = prompt; }
    public void setResponse(String response)         { this.response = response; }
    public void setTokens(Integer tokens)            { this.tokens = tokens; }
    public void setStatus(String status)             { this.status = status; }
    public void setTimestamp(LocalDateTime timestamp){ this.timestamp = timestamp; }
}