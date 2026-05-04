package com.prep.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
public class AIService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public AIService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.groq.com/openai/v1")
                .build();
    }

    private String callAI(String prompt) {

        System.out.println("🟡 Prompt: " + prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.1-8b-instant");

        // 🔥 REDUCED (IMPORTANT)
        requestBody.put("max_tokens", 400);
        requestBody.put("temperature", 0.7);

        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));

        try {
            return webClient.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()

                    // RATE LIMIT
                    .onStatus(status -> status.value() == 429, response ->
                            Mono.error(new RuntimeException("RATE_LIMIT"))
                    )

                    // OTHER ERRORS
                    .onStatus(status -> status.isError(), response ->
                            Mono.error(new RuntimeException("API_ERROR"))
                    )

                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})

                    // 🔥 SAFE TIMEOUT
                    .timeout(Duration.ofSeconds(20))

                    // 🔥 RETRY (LIMITED)
                    .retryWhen(
                            Retry.fixedDelay(1, Duration.ofSeconds(2))
                                    .filter(ex -> ex.getMessage() != null &&
                                            (ex.getMessage().contains("RATE_LIMIT") ||
                                             ex.getMessage().toLowerCase().contains("timeout")))
                    )

                    // 🔥 FALLBACK (CRITICAL)
                    .onErrorReturn(Map.of(
                            "choices", List.of(
                                    Map.of("message",
                                            Map.of("content", "⚠️ AI temporarily unavailable, try again.")))
                    ))

                    // EXTRACT RESPONSE
                    .map(res -> {
                        try {
                            List<Map<String, Object>> choices =
                                    (List<Map<String, Object>>) res.get("choices");

                            Map<String, Object> message =
                                    (Map<String, Object>) choices.get(0).get("message");

                            return message.get("content").toString();

                        } catch (Exception e) {
                            return "Error parsing AI response";
                        }
                    })

                    .block();

        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ AI service error";
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