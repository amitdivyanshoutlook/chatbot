package com.perplexity.perplexity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.perplexity.perplexity.model.GovernmentJobRequest;
import com.perplexity.perplexity.service.GovernmentJobService;
import com.perplexity.perplexity.service.UsageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/government-jobs")
public class GovernmentJobController {

    @Autowired
    private GovernmentJobService governmentJobService;

    @Autowired
    private UsageService usageService;

    @GetMapping
    public String showGovernmentJobsPage(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("jobRequest", new GovernmentJobRequest());
        return "government-jobs";
    }

    @PostMapping("/search")
    @ResponseBody
    public ResponseEntity<String> searchGovernmentJobs(@RequestBody GovernmentJobRequest request, HttpSession session) {
        System.out.println("=== GOVERNMENT JOBS REQUEST ===");
        System.out.println("Request: " + request.toString());

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("{\"error\": \"Please login to continue\"}");
        }

        // Check daily usage limit
        if (!usageService.canMakeRequest(userId)) {
            return ResponseEntity.status(429).body("{\"error\": \"Your daily usage limit is exhausted. Please try again tomorrow.\"}");
        }

        try {
            // Increment usage count
            usageService.incrementUsage(userId);

            String response = governmentJobService.fetchGovernmentJobs(request);
            System.out.println("Government Jobs Response: " + response);

            // Return the response as-is since the service should return JSON
            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            System.err.println("Error processing government jobs request: " + e.getMessage());
            return ResponseEntity.status(500).body("{\"error\": \"Error processing your request. Please try again.\"}");
        } catch (Exception e) {
            System.err.println("Unexpected error in government jobs request: " + e.getMessage());
            return ResponseEntity.status(500).body("{\"error\": \"An unexpected error occurred. Please try again.\"}");
        }
    }
}
