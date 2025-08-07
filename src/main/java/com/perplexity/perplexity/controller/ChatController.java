package com.perplexity.perplexity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.perplexity.perplexity.model.ChatRequest;
import com.perplexity.perplexity.model.User;
import com.perplexity.perplexity.service.PerplexityService;
import com.perplexity.perplexity.service.UsageService;
import com.perplexity.perplexity.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final PerplexityService service;
    
    @Autowired
    private UsageService usageService;

    @Autowired
    private UserService userService;
    


    public ChatController(PerplexityService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody String userPrompt, HttpSession session) throws JsonProcessingException {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Please login to continue");
        }
        
        // Check daily usage limit
        if (!usageService.canMakeRequest(userId)) {
            return ResponseEntity.status(429).body("Your daily usage limit is exhausted. Please try again tomorrow.");
        }
        
        // Increment usage count
        usageService.incrementUsage(userId);
        User user = userService.getUserById(userId);
        // Get response from AI (default to Hindi for backward compatibility)
        String response = service.fetchReply(userPrompt, user, "hindi");
        
        
        // Add remaining requests info
        int remaining = usageService.getRemainingRequests(userId);
        if (remaining <= 3) {
            response += "\n\n[आपके पास आज " + remaining + " प्रश्न बचे हैं]";
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/v2")
    public ResponseEntity<String> chatV2(@RequestBody ChatRequest chatRequest, HttpSession session) throws JsonProcessingException {
        System.out.println("=== CHAT V2 REQUEST ===");
        System.out.println("Message: " + chatRequest.getMessage());
        System.out.println("Language: " + chatRequest.getLanguage());
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Please login to continue");
        }
        
        // Check daily usage limit
        if (!usageService.canMakeRequest(userId)) {
            return ResponseEntity.status(429).body("Your daily usage limit is exhausted. Please try again tomorrow.");
        }
        
        // Increment usage count
        usageService.incrementUsage(userId);
        User user = userService.getUserById(userId);
        
        // Get language preference (default to Hindi if not specified)
        String language = chatRequest.getLanguage() != null ? chatRequest.getLanguage() : "hindi";
        System.out.println("Final language used: " + language);
        
        // Get response from AI
        String response = service.fetchReply(chatRequest.getMessage(), user, language);
        
        // Add remaining requests info based on language
        int remaining = usageService.getRemainingRequests(userId);
        if (remaining <= 3) {
            if ("english".equalsIgnoreCase(language)) {
                response += "\n\n[You have " + remaining + " questions remaining today]";
            } else {
                response += "\n\n[आपके पास आज " + remaining + " प्रश्न बचे हैं]";
            }
        }
        
        return ResponseEntity.ok(response);
    }
}
