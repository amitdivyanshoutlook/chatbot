package com.perplexity.perplexity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.perplexity.perplexity.model.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PerplexityService {

    private static final String API_URL = "https://api.perplexity.ai/chat/completions";
    private static final String API_KEY = "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";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${aadhya.eduverse.prompt:}")
    private String prompt;

    public String fetchReply(String userInput,  User user) throws JsonMappingException, JsonProcessingException {
        int age = user.getAge();
        Message userMessage = new Message("user", buildPromptWithAge(age)+" "+ userInput);
        RequestPayload payload = new RequestPayload("sonar-pro", List.of(userMessage));
        int maxRetries = 3;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                String jsonBody = mapper.writeValueAsString(payload);
                
                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8")))
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .build();
                
                Response response = client.newCall(request).execute();
                
                if (!response.isSuccessful()) {
                    if (attempt < maxRetries) {
                        System.out.println("API call failed (attempt " + attempt + "/" + maxRetries + "): " + response.code());
                        Thread.sleep(1000 * attempt); // Exponential backoff
                        continue;
                    }
                    return "Error: " + response.code() + " - " + response.message() + " (Failed after " + maxRetries + " attempts)";
                }
                
                String responseBody = new String(response.body().bytes(), java.nio.charset.StandardCharsets.UTF_8);
                System.out.println("API call successful on attempt " + attempt);
                
                ResponsePayload responsePayload = mapper.readValue(responseBody, ResponsePayload.class);
                if (responsePayload.getChoices() == null || responsePayload.getChoices().isEmpty()) {
                    if (attempt < maxRetries) {
                        System.out.println("Empty response (attempt " + attempt + "/" + maxRetries + ")");
                        Thread.sleep(1000 * attempt);
                        continue;
                    }
                    return "No response from the model after " + maxRetries + " attempts.";
                }
                
                // Find the first text content
                for (ResponsePayload.Choice choice : responsePayload.getChoices()) {
                    Message messageContent = choice.getMessage();
                    if (messageContent != null) {
                        String content = messageContent.getContent();
                        String cleaned = content.replaceAll("\\[\\d+\\]", "").replaceAll("\\*\\*(.*?)\\*\\*", "$1");
                        
                        if (cleaned != null && !cleaned.trim().isEmpty()) {
                            return cleaned;
                        }
                    }
                }
                
                // If we reach here, no valid content found
                if (attempt < maxRetries) {
                    System.out.println("No valid content found (attempt " + attempt + "/" + maxRetries + ")");
                    Thread.sleep(1000 * attempt);
                    continue;
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Request interrupted";
            } catch (Exception e) {
                if (attempt < maxRetries) {
                    System.out.println("Exception on attempt " + attempt + "/" + maxRetries + ": " + e.getMessage());
                    try {
                        Thread.sleep(1000 * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return "Request interrupted";
                    }
                    continue;
                }
                return "API call failed after " + maxRetries + " attempts: " + e.getMessage();
            }
        }
        
        return "Failed to get valid response after " + maxRetries + " attempts";
    }
    
    public String fetchReplyForSystem(String userInput) throws JsonMappingException, JsonProcessingException {
        // System call without user context - use default age-appropriate prompt
        Message userMessage = new Message("user", buildSystemPrompt() + " " + userInput);
        RequestPayload payload = new RequestPayload("sonar-pro", List.of(userMessage));
        int maxRetries = 3;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                String jsonBody = mapper.writeValueAsString(payload);
                
                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8")))
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .build();
                
                Response response = client.newCall(request).execute();
                
                if (!response.isSuccessful()) {
                    if (attempt < maxRetries) {
                        System.out.println("System API call failed (attempt " + attempt + "/" + maxRetries + "): " + response.code());
                        Thread.sleep(1000 * attempt);
                        continue;
                    }
                    return "Error: " + response.code() + " - " + response.message() + " (Failed after " + maxRetries + " attempts)";
                }
                
                String responseBody = new String(response.body().bytes(), java.nio.charset.StandardCharsets.UTF_8);
                System.out.println("System API call successful on attempt " + attempt);
                
                ResponsePayload responsePayload = mapper.readValue(responseBody, ResponsePayload.class);
                if (responsePayload.getChoices() == null || responsePayload.getChoices().isEmpty()) {
                    if (attempt < maxRetries) {
                        System.out.println("Empty system response (attempt " + attempt + "/" + maxRetries + ")");
                        Thread.sleep(1000 * attempt);
                        continue;
                    }
                    return "No response from the model after " + maxRetries + " attempts.";
                }
                
                // Find the first text content
                for (ResponsePayload.Choice choice : responsePayload.getChoices()) {
                    Message messageContent = choice.getMessage();
                    if (messageContent != null) {
                        String content = messageContent.getContent();
                        String cleaned = content.replaceAll("\\[\\d+\\]", "").replaceAll("\\*\\*(.*?)\\*\\*", "$1");
                        
                        if (cleaned != null && !cleaned.trim().isEmpty()) {
                            return cleaned;
                        }
                    }
                }
                
                // If we reach here, no valid content found
                if (attempt < maxRetries) {
                    System.out.println("No valid system content found (attempt " + attempt + "/" + maxRetries + ")");
                    Thread.sleep(1000 * attempt);
                    continue;
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Request interrupted";
            } catch (Exception e) {
                if (attempt < maxRetries) {
                    System.out.println("System exception on attempt " + attempt + "/" + maxRetries + ": " + e.getMessage());
                    try {
                        Thread.sleep(1000 * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return "Request interrupted";
                    }
                    continue;
                }
                return "System API call failed after " + maxRetries + " attempts: " + e.getMessage();
            }
        }
        
        return "Failed to get valid system response after " + maxRetries + " attempts";
    }
    
    private String buildSystemPrompt() {
        return "Please explain any concept in a simple and heartfelt way. First, give a clear and direct answer (in Devanagari/Hindi script). " +
                "Then, explain it like you're talking to a 10-year-old Indian student who speaks only Hindi at home. " +
                "Don't use boring, classroom-style language – speak like an elder brother, father, or grandmother would. " +
                "Use a real or imagined story or daily life example if needed. Blend Hindi and English naturally, like real conversations in Indian homes. " +
                "Avoid technical jargon or complicated definitions. Always keep the response in Hindi script and under 300 words. " +
                "The goal is not just to understand the concept, but to feel it deeply. " +
                "If the question is simple, skip the analogy and just give a clear explanation.";
    }

    private String buildPromptWithAge(int age) {
        return "Please explain any concept in a simple and heartfelt way. First, give a clear and direct answer (in Devanagari/Hindi script). " +
                "Then, explain it like you're talking to a " + age + "-year-old Indian student who speaks only Hindi at home. " +
                "Don’t use boring, classroom-style language – speak like an scholar explaining the things " +
                "Use a real life analogy if needed. Blend Hindi and English naturally, like real conversations in Indian homes. " +
                "Avoid technical jargon or complicated definitions. Always keep the response in Hindi script and under 200 words. " +
                "The goal is not just to understand the concept, but to feel it deeply. " +
                "If the question is simple, skip the analogy and just give a clear explanation.";
    }

}