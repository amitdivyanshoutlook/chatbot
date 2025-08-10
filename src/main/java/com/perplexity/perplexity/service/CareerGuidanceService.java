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
    
    public String getCareerGuidanceForSystem(String qualification, String language, String interests, String preferredField) throws JsonProcessingException {
        // Build the optimized prompt for faster response
        String prompt = buildOptimizedCareerPrompt(qualification, interests, preferredField, language);
        
        // Get AI-powered career guidance without user context
        return perplexityService.fetchReplyForSystem(prompt, language);
    }

    private String buildCareerGuidancePrompt(String qualification, String interests, String preferredField, String language) {
        StringBuilder prompt = new StringBuilder();

        if ("english".equalsIgnoreCase(language)) {
            prompt.append("Career guide for ").append(qualification).append(" (max 300 words): ");

            if (interests != null && !interests.trim().isEmpty()) {
                prompt.append("Interests: ").append(interests).append(". ");
            }
            if (preferredField != null && !preferredField.trim().isEmpty()) {
                prompt.append("Field: ").append(preferredField).append(". ");
            }

            prompt.append("Provide: ")
                    .append("1) Top 5 career paths; ")
                    .append("2) Best 3 institutions (national/international); ")
                    .append("3) Key skills needed; ")
                    .append("4) Salary range; ")
                    .append("5) Next steps. ")
                    .append("Be concise, use bullet points.");
        }
        else {
            prompt.append(qualification).append(" के लिए करियर गाइड (अधिकतम 300 शब्द): ");

            if (interests != null && !interests.trim().isEmpty()) {
                prompt.append("रुचि: ").append(interests).append("। ");
            }
            if (preferredField != null && !preferredField.trim().isEmpty()) {
                prompt.append("क्षेत्र: ").append(preferredField).append("। ");
            }

            prompt.append("दें: ")
                    .append("1) शीर्ष 5 करियर विकल्प; ")
                    .append("2) सर्वोत्तम 3 संस्थान (राष्ट्रीय/अंतर्राष्ट्रीय); ")
                    .append("3) मुख्य कौशल; ")
                    .append("4) वेतन सीमा; ")
                    .append("5) अगले कदम। ")
                    .append("संक्षिप्त रखें, बुलेट पॉइंट्स का उपयोग करें।");
        }

        return prompt.toString();
    }

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
            
            prompt.append(". Provide in 250 words: ")
                    .append("1) Top 3 career paths ")
                    .append("2) Best 2 institutions ")
                    .append("3) Key skills ")
                    .append("4) Salary range ")
                    .append("5) Next step. ")
                    .append("Use bullet points, be direct.");
        } else {
            prompt.append(qualification).append(" के लिए त्वरित करियर गाइड");
            
            if (interests != null && !interests.trim().isEmpty()) {
                prompt.append(", रुचि: ").append(interests);
            }
            if (preferredField != null && !preferredField.trim().isEmpty()) {
                prompt.append(", क्षेत्र: ").append(preferredField);
            }
            
            prompt.append("। 250 शब्दों में दें: ")
                    .append("1) शीर्ष 3 करियर ")
                    .append("2) सर्वोत्तम 2 संस्थान ")
                    .append("3) मुख्य कौशल ")
                    .append("4) वेतन ")
                    .append("5) अगला कदम। ")
                    .append("बुलेट पॉइंट्स, सीधे जवाब।");
        }

        return prompt.toString();
    }

    public List<String> getAllQualifications() {
        return careerOptionRepository.findAllQualifications();
    }

    public void addCareerOption(CareerOption careerOption) {
        careerOptionRepository.save(careerOption);
    }

    public List<CareerOption> getCareerOptionsByCategory(String category) {
        return careerOptionRepository.findByCategory(category);
    }
}