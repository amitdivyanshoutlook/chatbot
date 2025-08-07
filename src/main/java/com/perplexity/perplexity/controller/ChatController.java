package com.perplexity.perplexity.controller;


import com.perplexity.perplexity.service.PerplexityService;
import com.perplexity.perplexity.service.PerplexityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final PerplexityService service;

    public ChatController(PerplexityService service) {
        this.service = service;
    }

    @PostMapping
    public String chat(@RequestBody String userPrompt, @RequestParam(defaultValue = "en") String lang) {
        return service.fetchReply(userPrompt, lang);
    }
}
