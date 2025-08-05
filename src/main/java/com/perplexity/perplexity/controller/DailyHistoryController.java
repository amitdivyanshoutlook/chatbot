package com.perplexity.perplexity.controller;

import com.perplexity.perplexity.model.DailyHistory;
import com.perplexity.perplexity.service.DailyHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/daily-history")
public class DailyHistoryController {
    
    @Autowired
    private DailyHistoryService dailyHistoryService;
    
    /**
     * Get today's historical story
     */
    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> getTodaysHistory(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("status", "error");
                response.put("message", "User not authenticated");
                return ResponseEntity.status(401).body(response);
            }
            
            DailyHistory todaysHistory = dailyHistoryService.getTodaysHistory();
            
            response.put("status", "success");
            response.put("date", todaysHistory.getHistoryDate().format(DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("hi", "IN"))));
            response.put("content", todaysHistory.getContent());
            response.put("viewCount", todaysHistory.getViewCount());
            response.put("isNew", todaysHistory.getViewCount() == 1);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error fetching today's history: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to fetch today's history");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Get recent daily histories (last 30 days)
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentHistories(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("status", "error");
                response.put("message", "User not authenticated");
                return ResponseEntity.status(401).body(response);
            }
            
            List<DailyHistory> recentHistories = dailyHistoryService.getRecentDailyHistories();
            List<Map<String, Object>> historiesData = recentHistories.stream()
                    .map(this::convertToMap)
                    .collect(Collectors.toList());
            
            response.put("status", "success");
            response.put("histories", historiesData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error fetching recent histories: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to fetch recent histories");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Check if today's history is available
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getTodaysHistoryStatus(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("status", "error");
                response.put("message", "User not authenticated");
                return ResponseEntity.status(401).body(response);
            }
            
            boolean exists = dailyHistoryService.todaysHistoryExists();
            
            response.put("status", "success");
            response.put("todaysHistoryExists", exists);
            response.put("date", java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("hi", "IN"))));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error checking today's history status: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to check status");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Force regenerate today's history (for testing/admin purposes)
     */
    @PostMapping("/regenerate")
    public ResponseEntity<Map<String, Object>> regenerateTodaysHistory(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("status", "error");
                response.put("message", "User not authenticated");
                return ResponseEntity.status(401).body(response);
            }
            
            // Force regeneration by clearing today's entry
            dailyHistoryService.clearTodaysHistory();
            DailyHistory newHistory = dailyHistoryService.getTodaysHistory();
            
            response.put("status", "success");
            response.put("message", "Today's history regenerated successfully");
            response.put("date", newHistory.getHistoryDate().format(DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("hi", "IN"))));
            response.put("content", newHistory.getContent());
            response.put("viewCount", newHistory.getViewCount());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error regenerating today's history: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "Failed to regenerate today's history");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * Convert DailyHistory entity to Map for JSON response
     */
    private Map<String, Object> convertToMap(DailyHistory history) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", history.getId());
        map.put("date", history.getHistoryDate().format(DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("hi", "IN"))));
        map.put("content", history.getContent());
        map.put("viewCount", history.getViewCount());
        map.put("createdAt", history.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")));
        return map;
    }
}