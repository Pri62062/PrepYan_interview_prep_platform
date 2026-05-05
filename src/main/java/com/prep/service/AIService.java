package com.prep.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.*;

@Service
public class AIService {

    // 🔥 FIX: Direct ENV variable binding
    @Value("${GROQ_API_KEY}")
    private String groqApiKey;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL    = "llama3-8b-8192";
    private static final int    RETRIES  = 2;

    // ── MAIN ASK ──────────────────────────────
    public String askAI(String userMessage) {
        String system = "You are PrepYan AI, an expert interview tutor for Java, DSA, SQL, Python, System Design. " +
                "Give clear structured answers with examples. Max 300 words.";
        return callWithRetry(system, userMessage);
    }

    // ── CODE REVIEW ───────────────────────────
    public String reviewCode(String code) {
        String prompt = "Review this code for bugs, performance and best practices. Give corrected version:\n\n" + code;
        return callWithRetry("You are a senior Java developer doing code review.", prompt);
    }

    // ── EXPLAIN CONCEPT ───────────────────────
    public String explainConcept(String topic) {
        String prompt = "Explain for Java interview: " + topic + ". Include definition, internals, and use cases.";
        return callWithRetry("You are PrepYan AI interview tutor.", prompt);
    }

    // ── ANSWER FEEDBACK ───────────────────────
    public String evaluateAnswer(String question, String userAnswer) {
        String prompt = String.format(
                "Evaluate this interview answer.\nQuestion: %s\nAnswer: %s\n\n" +
                "Give: Score/10, What was good, What was missing, Model answer.",
                question, userAnswer);
        return callWithRetry("You are an expert interview evaluator.", prompt);
    }

    // ── RETRY WRAPPER ─────────────────────────
    private String callWithRetry(String system, String userMessage) {
        Exception lastErr = null;

        for (int i = 1; i <= RETRIES; i++) {
            try {
                return callGroq(system, userMessage);
            } catch (ResourceAccessException e) {
                lastErr = e;

                if (i < RETRIES) {
                    try {
                        Thread.sleep(2000L * i);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        throw new RuntimeException("AI unavailable after retries: " +
                (lastErr != null ? lastErr.getMessage() : "unknown"));
    }

    // ── GROQ API CALL ─────────────────────────
    @SuppressWarnings("unchecked")
    private String callGroq(String system, String userMessage) {

        // 🔥 DEBUG (optional - remove later)
        System.out.println("GROQ KEY: " + groqApiKey);

        if (groqApiKey == null || groqApiKey.isBlank()) {
            throw new RuntimeException("GROQ API KEY is missing!");
        }

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(20_000);
        factory.setReadTimeout(60_000);

        RestTemplate rest = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", MODEL);
        body.put("temperature", 0.7);
        body.put("max_tokens", 600);
        body.put("messages", List.of(
                Map.of("role", "system", "content", system),
                Map.of("role", "user", "content", userMessage)
        ));

        ResponseEntity<Map> resp = rest.exchange(
                GROQ_URL,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        // 🔥 STATUS CHECK (IMPORTANT)
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Groq API error: " + resp.getStatusCode());
        }

        if (resp.getBody() == null) {
            throw new RuntimeException("Empty response from Groq");
        }

        try {
            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) resp.getBody().get("choices");

            Map<String, Object> msg =
                    (Map<String, Object>) choices.get(0).get("message");

            return (String) msg.get("content");

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response: " + e.getMessage());
        }
    }
}