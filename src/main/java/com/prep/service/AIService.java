package com.prep.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.*;

/**
 * ✅ Memory-safe AI Service for Render 256MB
 *
 * Changes from previous version:
 * 1. New RestTemplate created per request — NOT stored as field
 *    (prevents memory leak from keeping connections open)
 * 2. max_tokens reduced to 600 (was 1024) — smaller responses = less heap
 * 3. Response parsed immediately and intermediate objects GC-able
 * 4. System prompt shortened — less token usage = faster + cheaper
 */
@Service
public class AIService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL    = "llama3-8b-8192";
    private static final int    RETRIES  = 2;

    // ── Main ask ──────────────────────────────
    public String askAI(String userMessage) {
        String system = "You are PrepYan AI, an expert interview tutor for Java, DSA, SQL, Python, System Design. " +
                "Give clear structured answers with code examples. Max 350 words unless asked for more.";
        return callWithRetry(system, userMessage);
    }

    // ── Code Review ───────────────────────────
    public String reviewCode(String code) {
        String prompt = "Review this code for bugs, performance and best practices. Give corrected version:\n\n" + code;
        return callWithRetry("You are a senior Java developer doing code review.", prompt);
    }

    // ── Explain Concept ───────────────────────
    public String explainConcept(String topic) {
        String prompt = "Explain for Java interview: " + topic + ". Include definition, internals, use cases.";
        return callWithRetry("You are PrepYan AI interview tutor.", prompt);
    }

    // ── Answer Feedback ───────────────────────
    public String evaluateAnswer(String question, String userAnswer) {
        String prompt = String.format(
                "Evaluate this interview answer.\nQuestion: %s\nAnswer: %s\n\n" +
                "Give: Score/10, What was good, What was missing, Model answer.",
                question, userAnswer);
        return callWithRetry("You are an expert interview evaluator.", prompt);
    }

    // ── Retry wrapper ─────────────────────────
    private String callWithRetry(String system, String userMessage) {
        Exception lastErr = null;
        for (int i = 1; i <= RETRIES; i++) {
            try {
                return callGroq(system, userMessage);
            } catch (ResourceAccessException e) {
                lastErr = e;
                if (i < RETRIES) {
                    try { Thread.sleep(2000L * i); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                }
            }
        }
        throw new RuntimeException("AI unavailable after " + RETRIES + " attempts: " +
                (lastErr != null ? lastErr.getMessage() : "unknown"));
    }

    // ── Actual Groq call ──────────────────────
    @SuppressWarnings("unchecked")
    private String callGroq(String system, String userMessage) {
        // ✅ New factory per request — not cached in memory
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(20_000);
        factory.setReadTimeout(60_000);
        RestTemplate rest = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        // ✅ max_tokens 600 (was 1024) — saves heap on large responses
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model",       MODEL);
        body.put("temperature", 0.7);
        body.put("max_tokens",  600);
        body.put("messages", List.of(
                Map.of("role", "system", "content", system),
                Map.of("role", "user",   "content", userMessage)
        ));

        ResponseEntity<Map> resp = rest.exchange(
                GROQ_URL, HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        if (resp.getBody() == null) throw new RuntimeException("Empty response from Groq");

        try {
            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) resp.getBody().get("choices");
            Map<String, Object> msg = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) msg.get("content");

            // ✅ Explicitly null out large objects for GC
            choices = null;
            msg = null;
            body = null;

            return content;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response: " + e.getMessage());
        }
    }
}