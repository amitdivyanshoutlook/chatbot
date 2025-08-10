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
        // Build the prompt for Perplexity API (without database dependency)
        String prompt = buildCareerGuidancePrompt(qualification, interests, preferredField, language);
        
        // Get AI-powered career guidance
        return perplexityService.fetchReply(prompt, user, language);
    }
    
    public String getCareerGuidanceForSystem(String qualification, String language, String interests, String preferredField) throws JsonProcessingException {
        // Build the prompt for Perplexity API (without database dependency)
        String prompt = buildCareerGuidancePrompt(qualification, interests, preferredField, language);
        
        // Get AI-powered career guidance without user context
        return perplexityService.fetchReplyForSystem(prompt, language);
    }

    private String buildCareerGuidancePrompt(String qualification, String interests, String preferredField, String language) {
        StringBuilder prompt = new StringBuilder();

        if ("english".equalsIgnoreCase(language)) {
            prompt.append("Provide a career guide (max 400 words) for someone with ")
                    .append(qualification).append(". ");

            if (interests != null && !interests.trim().isEmpty()) {
                prompt.append("Interests: ").append(interests).append(". ");
            }
            if (preferredField != null && !preferredField.trim().isEmpty()) {
                prompt.append("Preferred field: ").append(preferredField).append(". ");
            }

            prompt.append("Include: ")
                    .append("1) Top 5–7 career options after ").append(qualification).append(" with brief details; ")
                    .append("2) Higher study paths (degrees, certifications, durations); ")
                    .append("3) Best national & international institutions with entry requirements; ")
                    .append("4) Jobs/internships; ")
                    .append("5) Skills & certifications to gain; ")
                    .append("6) Industry trends & future scope; ")
                    .append("7) Salary & growth potential; ")
                    .append("8) Immediate next steps. ")
                    .append("Use headings & bullet points.");
        }
        else {
            prompt.append(qualification).append(" की योग्यता वाले व्यक्ति के लिए अधिकतम 400 शब्दों में करियर गाइड दें। ");

            if (interests != null && !interests.trim().isEmpty()) {
                prompt.append("रुचियां: ").append(interests).append("। ");
            }
            if (preferredField != null && !preferredField.trim().isEmpty()) {
                prompt.append("पसंदीदा क्षेत्र: ").append(preferredField).append("। ");
            }

            prompt.append("शामिल करें: ")
                    .append("1) ").append(qualification).append(" के बाद शीर्ष 5–7 करियर विकल्प; ")
                    .append("2) उच्च शिक्षा पथ (डिग्री, प्रमाणपत्र, अवधि); ")
                    .append("3) सर्वोत्तम राष्ट्रीय व अंतर्राष्ट्रीय संस्थान व प्रवेश आवश्यकताएं; ")
                    .append("4) नौकरियां/इंटर्नशिप; ")
                    .append("5) कौशल व प्रमाणपत्र; ")
                    .append("6) उद्योग रुझान व भविष्य; ")
                    .append("7) वेतन व वृद्धि; ")
                    .append("8) तुरंत उठाए जाने वाले कदम। ")
                    .append("शीर्षक व बुलेट पॉइंट्स का उपयोग करें।");
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