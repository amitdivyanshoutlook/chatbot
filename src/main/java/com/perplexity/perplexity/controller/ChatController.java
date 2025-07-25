package com.perplexity.perplexity.controller;

import com.perplexity.perplexity.service.PerplexityService;
import com.perplexity.perplexity.service.UsageService;
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

    public ChatController(PerplexityService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody String userPrompt, HttpSession session) {
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
        
        // Get response from AI
        String response = service.fetchReply(userPrompt);
        
        // Add remaining requests info
        int remaining = usageService.getRemainingRequests(userId);
        if (remaining <= 3) {
            response += "\n\n[आपके पास आज " + remaining + " प्रश्न बचे हैं]";
        }
        
        return ResponseEntity.ok(response);
    }
}
