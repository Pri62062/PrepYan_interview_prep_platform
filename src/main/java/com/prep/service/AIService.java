package com.prep.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private String callAI(String prompt) {

        System.out.println("🟡 Prompt: " + prompt);

        String url = "https://api.groq.com/openai/v1/chat/completions";

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.1-8b-instant");
        body.put("max_tokens", 400);
        body.put("temperature", 0.7);

        body.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, request, Map.class);

            Map res = response.getBody();

            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) res.get("choices");

            Map<String, Object> message =
                    (Map<String, Object>) choices.get(0).get("message");

            return message.get("content").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ AI temporarily unavailable";
        }
    }

    public String askAI(String question) {
        return callAI(question);
    }

    public String getFeedback(String answer) {
        return callAI("Give constructive feedback:\n" + answer);
    }

    public String explain(String question) {
        return callAI("Explain in simple terms:\n" + question);
    }

    public String reviewCode(String code) {
        return callAI("Review this code:\n" + code);
    }
}