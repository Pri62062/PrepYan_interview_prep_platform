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

    // 🔥 COMMON METHOD
    private String callAI(String prompt) {

        System.out.println("🟡 Prompt: " + prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.1-8b-instant");
        requestBody.put("max_tokens", 1000);
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

                    // 🔥 HANDLE RATE LIMIT (429)
                    .onStatus(status -> status.value() == 429, response ->
                            response.bodyToMono(String.class)
                                    .flatMap(error -> {
                                        System.out.println("⚠️ Rate limit hit: " + error);
                                        return Mono.error(new RuntimeException("RATE_LIMIT"));
                                    })
                    )

                    // ❌ OTHER ERRORS
                    .onStatus(status -> status.isError(), response ->
                            response.bodyToMono(String.class)
                                    .flatMap(error -> {
                                        System.out.println("❌ API Error: " + error);
                                        return Mono.error(new RuntimeException("API_ERROR"));
                                    })
                    )

                    // ✅ RESPONSE TYPE
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})

                    // 🔥 TIMEOUT FIX (IMPORTANT)
                    .timeout(Duration.ofSeconds(40))

                    // 🔥 RETRY (RATE LIMIT + TIMEOUT)
                    .retryWhen(
                            Retry.fixedDelay(2, Duration.ofSeconds(3))
                                    .filter(ex ->
                                            ex.getMessage() != null &&
                                            (ex.getMessage().contains("RATE_LIMIT") ||
                                             ex.getMessage().toLowerCase().contains("timeout"))
                                    )
                    )

                    // 🔍 DEBUG LOGS
                    .doOnError(err -> System.out.println("🔥 ERROR: " + err.getMessage()))
                    .doOnSuccess(res -> System.out.println("✅ SUCCESS RESPONSE"))

                    // ✅ EXTRACT RESPONSE
                    .map(res -> {
                        try {
                            List<Map<String, Object>> choices =
                                    (List<Map<String, Object>>) res.get("choices");

                            if (choices == null || choices.isEmpty()) {
                                return "No response from AI";
                            }

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

            if (e.getMessage() != null) {

                if (e.getMessage().contains("RATE_LIMIT")) {
                    return "⚠️ Too many requests. Please wait and try again.";
                }

                if (e.getMessage().toLowerCase().contains("timeout")) {
                    return "⚠️ AI is taking too long. Please try again.";
                }
            }

            return "❌ Error calling AI API. Try again.";
        }
    }

    // 🔥 MAIN ASK METHOD
    public String askAI(String question) {
        return callAI(question);
    }

    // ✅ FEEDBACK METHOD
    public String getFeedback(String answer) {
        String prompt = "Give constructive feedback for this answer:\n" + answer;
        return callAI(prompt);
    }

    // ✅ EXPLAIN METHOD
    public String explain(String question) {
        String prompt = "Explain this in simple terms:\n" + question;
        return callAI(prompt);
    }

    // 🔥 CODE REVIEW METHOD
    public String reviewCode(String code) {
        String prompt = "Review the following code and suggest improvements:\n" + code;
        return callAI(prompt);
    }
}