package com.perplexity.perplexity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.perplexity.perplexity.model.*;
import okhttp3.*;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Service
public class PerplexityService {

    private static final String API_URL = "https://api.perplexity.ai/chat/completions";
    private static final String API_KEY = "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";
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final TranslateService translateService;
    private final MessageSource messageSource;

    public PerplexityService(TranslateService translateService, MessageSource messageSource) {
        this.translateService = translateService;
        this.messageSource = messageSource;
    }

    public String fetchReply(String userInput, String lang) {

        try {
            Locale locale = new Locale(lang);
            String prompt = messageSource.getMessage("prompt.text", null, locale);

            if ("hi".equals(lang)) {
                userInput = translateService.translate(userInput, "hi", "en");
                prompt = translateService.translate(prompt, "hi", "en");
            }

            Message userMessage = new Message("user", prompt + " " + userInput);
            RequestPayload payload = new RequestPayload("sonar-pro", List.of(userMessage));

            String jsonBody = mapper.writeValueAsString(payload);

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(RequestBody.create(jsonBody, MediaType.get("application/json")))
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Accept", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                return "Error: " + response.code() + " - " + response.message();
            }
            String responseBody = response.body().string();
            System.out.println(responseBody+ " response");
            ResponsePayload responsePayload = mapper.readValue(responseBody, ResponsePayload.class);
            if (responsePayload.getChoices() == null || responsePayload.getChoices().isEmpty()) {
                return "No response from the model.";
            }
           
            // Find the first text content
            for (ResponsePayload.Choice choice : responsePayload.getChoices()) {
                Message messageContent = choice.getMessage();
                if (messageContent != null) {
                    String content = messageContent.getContent();
                    // Remove markdown footnotes like [1], [2], etc.
                    String cleaned = content.replaceAll("\\[\\d+\\]", "").replaceAll("\\*\\*(.*?)\\*\\*", "$1");


                    if (cleaned != null) {
                        if ("hi".equals(lang)) {
                            return translateService.translate(cleaned, "en", "hi");
                        }
                        return cleaned;
                    }
                }
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}