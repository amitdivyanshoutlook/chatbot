package com.perplexity.perplexity.controller;

import com.perplexity.perplexity.service.PerplexityService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final PerplexityService service;

    public ChatController(PerplexityService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody String userPrompt, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Please login to continue");
        }
        return ResponseEntity.ok(service.fetchReply(userPrompt));
    }
}
