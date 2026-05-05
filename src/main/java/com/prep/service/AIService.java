package com.prep.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.*;

@Service
public class AIService {

    // ✅ FIXED: Direct ENV read (Render safe)
    private final String groqApiKey = System.getenv("GROQ_API_KEY");

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "mixtral-8x7b-32768";

    private static final int RETRIES = 3;

    // ─────────────────────────────────────────
    // 🔥 MAIN ASK
    // ─────────────────────────────────────────
    public String askAI(String userMessage) {
        String system = "You are PrepYan AI, an expert interview tutor for Java, DSA, SQL, Python, System Design. " +
                "Give clear structured answers with examples. Max 300 words.";
        return callWithRetry(system, userMessage);
    }

    // ─────────────────────────────────────────
    // 🔥 CODE REVIEW
    // ─────────────────────────────────────────
    public String reviewCode(String code) {
        String prompt = "Review this code for bugs, performance and best practices. Give corrected version:\n\n" + code;
        return callWithRetry("You are a senior Java developer doing code review.", prompt);
    }

    // ─────────────────────────────────────────
    // 🔥 EXPLAIN CONCEPT
    // ─────────────────────────────────────────
    public String explainConcept(String topic) {
        String prompt = "Explain for Java interview: " + topic + ". Include definition, internals, and use cases.";
        return callWithRetry("You are PrepYan AI interview tutor.", prompt);
    }

    // ─────────────────────────────────────────
    // 🔥 ANSWER FEEDBACK
    // ─────────────────────────────────────────
    public String evaluateAnswer(String question, String userAnswer) {
        String prompt = String.format(
                "Evaluate this interview answer.\nQuestion: %s\nAnswer: %s\n\n" +
                        "Give: Score/10, What was good, What was missing, Model answer.",
                question, userAnswer);
        return callWithRetry("You are an expert interview evaluator.", prompt);
    }

    // ─────────────────────────────────────────
    // 🔁 RETRY WRAPPER (STRONG)
    // ─────────────────────────────────────────
    private String callWithRetry(String system, String userMessage) {

        Exception lastErr = null;

        for (int i = 1; i <= RETRIES; i++) {
            try {
                return callGroq(system, userMessage);
            } catch (Exception e) {
                lastErr = e;

                // 🔁 exponential backoff
                try {
                    Thread.sleep(2000L * i);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        throw new RuntimeException("AI unavailable after retries: " +
                (lastErr != null ? lastErr.getMessage() : "unknown"));
    }

    // ─────────────────────────────────────────
    // 🌐 GROQ API CALL
    // ─────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private String callGroq(String system, String userMessage) {

        // ❌ SAFETY CHECK
        if (groqApiKey == null || groqApiKey.isBlank()) {
            throw new RuntimeException("GROQ_API_KEY is missing in environment variables!");
        }

        // ✅ TIMEOUT (Render optimized)
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);
        factory.setReadTimeout(30_000);

        RestTemplate rest = new RestTemplate(factory);

        // ✅ HEADERS
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        // ✅ BODY
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", MODEL);
        body.put("temperature", 0.7);
        body.put("max_tokens", 600);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", system));
        messages.add(Map.of("role", "user", "content", userMessage));

        body.put("messages", messages);

        // ✅ REQUEST
        ResponseEntity<Map> resp = rest.exchange(
                GROQ_URL,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        // ❌ STATUS CHECK
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Groq API error: " + resp.getStatusCode());
        }

        if (resp.getBody() == null) {
            throw new RuntimeException("Empty response from Groq API");
        }

        // ✅ SAFE PARSE
        try {
            Object choicesObj = resp.getBody().get("choices");

            if (!(choicesObj instanceof List)) {
                throw new RuntimeException("Invalid response format (choices missing)");
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) choicesObj;

            if (choices.isEmpty()) {
                throw new RuntimeException("No choices returned from Groq");
            }

            Object messageObj = choices.get(0).get("message");

            if (!(messageObj instanceof Map)) {
                throw new RuntimeException("Invalid message format");
            }

            Map<String, Object> message = (Map<String, Object>) messageObj;

            Object content = message.get("content");

            if (content == null) {
                throw new RuntimeException("Empty AI response content");
            }

            return content.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Groq response: " + e.getMessage());
        }
    }
}