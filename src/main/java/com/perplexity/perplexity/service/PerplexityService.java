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

    public String fetchReply(String userInput) {
        Message userMessage = new Message("user", prompt+" "+ userInput);
        RequestPayload payload = new RequestPayload("sonar-pro", List.of(userMessage));
        int maxRetries = 3;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                String jsonBody = mapper.writeValueAsString(payload);
                
                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .addHeader("Accept", "application/json")
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
                
                String responseBody = response.body().string();
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
}