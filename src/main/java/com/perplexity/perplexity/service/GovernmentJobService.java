package com.perplexity.perplexity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.perplexity.perplexity.model.*;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class GovernmentJobService {

    private static final String API_URL = "https://api.perplexity.ai/chat/completions";
    private static final String API_KEY = "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";
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .build();
    private final ObjectMapper mapper = new ObjectMapper();

    public String fetchGovernmentJobs(GovernmentJobRequest request) throws JsonProcessingException {
        String prompt = buildGovernmentJobPrompt(request);
        Message userMessage = new Message("user", prompt);
        RequestPayload payload = new RequestPayload("sonar-pro", List.of(userMessage));
        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                String jsonBody = mapper.writeValueAsString(payload);

                Request httpRequest = new Request.Builder()
                        .url(API_URL)
                        .post(RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8")))
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .build();

                Response response = client.newCall(httpRequest).execute();

                if (!response.isSuccessful()) {
                    if (attempt < maxRetries) {
                        System.out.println("Government Jobs API call failed (attempt " + attempt + "/" + maxRetries + "): " + response.code());
                        Thread.sleep(1000 * attempt);
                        continue;
                    }
                    return "Error: " + response.code() + " - " + response.message() + " (Failed after " + maxRetries + " attempts)";
                }

                String responseBody = new String(response.body().bytes(), java.nio.charset.StandardCharsets.UTF_8);
                System.out.println("Government Jobs API call successful on attempt " + attempt);

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
                        if (content != null && !content.trim().isEmpty()) {
                            return content;
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
            } catch (java.net.SocketTimeoutException e) {
                if (attempt < maxRetries) {
                    System.out.println("Timeout on attempt " + attempt + "/" + maxRetries + ": " + e.getMessage());
                    try {
                        Thread.sleep(2000 * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return "Request interrupted";
                    }
                    continue;
                }
                return "Request timed out after " + maxRetries + " attempts. Please try again.";
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

    private String buildGovernmentJobPrompt(GovernmentJobRequest request) {
        return "List all current government job openings in India for a student with the following profile:\n" +
                "- Qualification: " + request.getQualification() + "\n" +
                "- Field of study: " + request.getFieldOfStudy() + "\n" +
                "- Age: " + request.getAge() + "\n" +
                "- Location preference: " + request.getLocation() + "\n" +
                "- Job type preference: " + request.getJobType() + "\n\n" +
                "Give the response in a clean JSON format with the following structure:\n" +
                "{\n" +
                "  \"jobs\": [\n" +
                "    {\n" +
                "      \"job_title\": \"Job Title\",\n" +
                "      \"department_organization\": \"Department/Organization Name\",\n" +
                "      \"qualification_required\": \"Required Qualification\",\n" +
                "      \"last_date_to_apply\": \"Last Date\",\n" +
                "      \"application_link\": \"Application URL\",\n" +
                "      \"location\": \"Job Location\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n\n" +
                "Provide only the JSON response without any additional text or formatting. " +
                "Include only currently open and active government job postings that match the profile. " +
                "Jobs's last date  to apply shouldn't passed today's datee"+
                "If no specific jobs are available, provide general categories of government jobs suitable for the profile.";
    }
}
