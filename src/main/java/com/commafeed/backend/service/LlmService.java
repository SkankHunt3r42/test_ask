package com.commafeed.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class LlmService {

    @ConfigProperty(name = "app.llm.api.key")
    Optional<String> apiKey;

    @ConfigProperty(
            name = "app.llm.api.url",
            defaultValue = "https://api.openai.com/v1/chat/completions")
    String apiUrl;

    @ConfigProperty(name = "app.llm.api.model", defaultValue = "gpt-3.5-turbo")
    String model;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient =
            HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    public String generateAlternative(String content, String prompt)
            throws IOException, InterruptedException {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", model);

        ArrayNode messages = requestBody.putArray("messages");

        ObjectNode systemMessage = messages.addObject();
        systemMessage.put("role", "system");
        systemMessage.put(
                "content",
                "You are a helpful assistant that rewrites text according to instructions. Provide only the rewritten text in your response, without any extra commentary.");

        ObjectNode userMessage = messages.addObject();
        userMessage.put("role", "user");
        userMessage.put("content", prompt + "\n\nText to rewrite:\n" + content);

        String jsonBody = objectMapper.writeValueAsString(requestBody);

        HttpRequest.Builder requestBuilder =
                HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .timeout(Duration.ofSeconds(60))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

        apiKey.filter(k -> !k.isBlank())
                .ifPresent(k -> requestBuilder.header("Authorization", "Bearer " + k));

        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode choices = rootNode.path("choices");
            if (choices.isArray() && !choices.isEmpty()) {
                return choices.get(0).path("message").path("content").asText();
            }
            throw new IOException("Unexpected LLM response format: " + response.body());
        } else {
            throw new IOException(
                    "LLM API error: HTTP " + response.statusCode() + " - " + response.body());
        }
    }
}
