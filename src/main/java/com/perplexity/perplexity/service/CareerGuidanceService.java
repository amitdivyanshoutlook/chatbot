package com.perplexity.perplexity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.perplexity.perplexity.model.CareerOption;
import com.perplexity.perplexity.model.User;
import com.perplexity.perplexity.repository.CareerOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareerGuidanceService {

    @Autowired
    private CareerOptionRepository careerOptionRepository;
    
    @Autowired
    private PerplexityService perplexityService;

    public String getCareerGuidance(String qualification, String language, String interests, String preferredField, User user) throws JsonProcessingException {
        // Build the optimized prompt for faster response
        String prompt = buildOptimizedCareerPrompt(qualification, interests, preferredField, language);
        
        // Get AI-powered career guidance
        return perplexityService.fetchReply(prompt, user, language);
    }
    
 /*   public String getCareerGuidanceForSystem(String qualification, String language, String interests, String preferredField) throws JsonProcessingException {
        // Build the optimized prompt for faster response
        String prompt = buildOptimizedCareerPrompt(qualification, interests, preferredField, language);
        
        // Get AI-powered career guidance without user context
        return perplexityService.fetchReplyForSystem(prompt, language);
    }*/



    private String buildOptimizedCareerPrompt(String qualification, String interests, String preferredField, String language) {
        StringBuilder prompt = new StringBuilder();

        if ("english".equalsIgnoreCase(language)) {
            prompt.append("Quick career guide for ").append(qualification);
            
            if (interests != null && !interests.trim().isEmpty()) {
                prompt.append(" with interest in ").append(interests);
            }
            if (preferredField != null && !preferredField.trim().isEmpty()) {
                prompt.append(" focusing on ").append(preferredField);
            }
            
            prompt.append(". Provide in 250 words in context of India: ")
                    .append("1) Top 3 career paths ")
                    .append("2) Best 5 institutions ")
                    .append("3) Salary range ")
                    .append("4) Next step. ")
                    .append("Use bullet points, be direct.");
        } else {
            prompt.append(qualification).append(" के लिए त्वरित करियर गाइड");
            
            if (interests != null && !interests.trim().isEmpty()) {
                prompt.append(", रुचि: ").append(interests);
            }
            if (preferredField != null && !preferredField.trim().isEmpty()) {
                prompt.append(", क्षेत्र: ").append(preferredField);
            }
            
            prompt.append("। 250 शब्दों में दें  in context of India: ")
                    .append("1) शीर्ष 3 करियर ")
                    .append("2) सर्वोत्तम 5 संस्थान ")
                    .append("3) वेतन ")
                    .append("4) अगला कदम। ")
                    .append("बुलेट पॉइंट्स, सीधे जवाब।");
        }

        return prompt.toString();
    }


}