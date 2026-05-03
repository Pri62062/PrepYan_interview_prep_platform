package com.prep.dto;

/**
 * Payload sent internally after an AI tutor interaction.
 * POST /api/ai/chat  (saves the log automatically)
 */
public class AiLogRequestDTO {

    private String  prompt;     // user's message
    private String  response;   // AI reply
    private Integer tokens;     // total tokens used
    private String  status;     // "completed" | "error"

    public String  getPrompt()              { return prompt; }
    public void    setPrompt(String p)      { this.prompt = p; }

    public String  getResponse()            { return response; }
    public void    setResponse(String r)    { this.response = r; }

    public Integer getTokens()              { return tokens; }
    public void    setTokens(Integer t)     { this.tokens = t; }

    public String  getStatus()              { return status; }
    public void    setStatus(String s)      { this.status = s; }
}