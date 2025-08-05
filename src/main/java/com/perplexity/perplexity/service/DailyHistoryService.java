package com.perplexity.perplexity.service;

import com.perplexity.perplexity.model.DailyHistory;
import com.perplexity.perplexity.repository.DailyHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class DailyHistoryService {
    
    @Autowired
    private DailyHistoryRepository dailyHistoryRepository;
    
    @Autowired
    private PerplexityService perplexityService;
    
    /**
     * Get today's historical story. If not exists, generate and save it.
     */
    public DailyHistory getTodaysHistory() {
        LocalDate today = LocalDate.now();
        
        // Check if today's history already exists
        Optional<DailyHistory> existingHistory = dailyHistoryRepository.findByHistoryDate(today);
        
        if (existingHistory.isPresent()) {
            DailyHistory history = existingHistory.get();
            
            // Check if content is corrupted (contains question marks indicating encoding issues)
            if (history.getContent() != null && (history.getContent().contains("???") || history.getContent().contains("?"))) {
                System.out.println("Detected corrupted content, regenerating...");
                // Delete corrupted entry and regenerate
                dailyHistoryRepository.delete(history);
                // Also clear any other corrupted entries
                clearCorruptedEntries();
                return generateTodaysHistory(today);
            }
            
            // Increment view count only (don't regenerate for existing users)
            dailyHistoryRepository.incrementViewCount(history.getId());
            history.incrementViewCount(); // Update in memory for immediate response
            System.out.println("Returning existing daily history for: " + today + " (View count: " + history.getViewCount() + ")");
            return history;
        }
        
        // Generate new daily history
        return generateTodaysHistory(today);
    }
    
    /**
     * Clear today's history entry (for regeneration)
     */
    public void clearTodaysHistory() {
        LocalDate today = LocalDate.now();
        Optional<DailyHistory> existingHistory = dailyHistoryRepository.findByHistoryDate(today);
        if (existingHistory.isPresent()) {
            dailyHistoryRepository.delete(existingHistory.get());
            System.out.println("Cleared today's history for regeneration: " + today);
        }
    }
    
    /**
     * Clear all corrupted entries from database
     */
    private void clearCorruptedEntries() {
        try {
            List<DailyHistory> allHistories = dailyHistoryRepository.findAll();
            for (DailyHistory history : allHistories) {
                if (history.getContent() != null && (history.getContent().contains("???") || history.getContent().contains("?"))) {
                    System.out.println("Deleting corrupted entry for date: " + history.getHistoryDate());
                    dailyHistoryRepository.delete(history);
                }
            }
        } catch (Exception e) {
            System.err.println("Error clearing corrupted entries: " + e.getMessage());
        }
    }
    
    /**
     * Generate today's historical story using AI
     */
    private DailyHistory generateTodaysHistory(LocalDate date) {
        try {
            System.out.println("Generating new daily history for: " + date);
            
            // Create the prompt for today's date
            String prompt = createHistoryPrompt(date);
            
            // Get response from AI (using a dummy user for system calls)
            String response = perplexityService.fetchReplyForSystem(prompt);
            
            // Debug logging
            System.out.println("AI Response received: " + (response != null ? response.substring(0, Math.min(100, response.length())) + "..." : "null"));
            System.out.println("Response encoding check - contains Hindi: " + (response != null && response.matches(".*[\\u0900-\\u097F].*")));
            
            if (response == null || response.trim().isEmpty()) {
                response = getDefaultHistoryMessage(date);
            }
            
            // Save to database
            DailyHistory dailyHistory = new DailyHistory(date, response);
            dailyHistory.setViewCount(1L); // First view
            DailyHistory saved = dailyHistoryRepository.save(dailyHistory);
            
            System.out.println("Daily history generated and saved for: " + date);
            return saved;
            
        } catch (Exception e) {
            System.err.println("Error generating daily history: " + e.getMessage());
            
            // Return default message on error
            String defaultMessage = getDefaultHistoryMessage(date);
            DailyHistory dailyHistory = new DailyHistory(date, defaultMessage);
            dailyHistory.setViewCount(1L);
            return dailyHistoryRepository.save(dailyHistory);
        }
    }
    
    /**
     * Create AI prompt for historical significance of the date
     */
    private String createHistoryPrompt(LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("d MMMM", new Locale("hi", "IN")));
        
        return "Tell me in a story form why " + dateStr + " is important in Indian history, " +
               "as if you're explaining it to a 20-year-old child. The language should be simple, " +
               "emotional, and memorable. Let it flow like a poem — with a touch of love, a sense of pride, " +
               "and warmth like an scholar telling a bedtime story. Make it so touching and " +
               "beautiful that I never forget it for the rest of my life. Please respond in Hindi (Devanagari script).";
    }
    
    /**
     * Get default message when AI fails
     */
    private String getDefaultHistoryMessage(LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("hi", "IN")));
        
        return "आज " + dateStr + " का दिन है। हर दिन भारतीय इतिहास में कुछ न कुछ खास होता है। " +
               "आज का दिन भी अपने आप में विशेष है। हमारे देश की महान परंपरा और संस्कृति को " +
               "आगे बढ़ाने का दिन है। आइए मिलकर इस दिन को यादगार बनाते हैं।";
    }
    
    /**
     * Get recent daily histories (last 30 days)
     */
    public List<DailyHistory> getRecentDailyHistories() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        return dailyHistoryRepository.findRecentDailyHistories(thirtyDaysAgo);
    }
    
    /**
     * Get daily history by specific date
     */
    public Optional<DailyHistory> getHistoryByDate(LocalDate date) {
        return dailyHistoryRepository.findByHistoryDate(date);
    }
    
    /**
     * Check if today's history exists
     */
    public boolean todaysHistoryExists() {
        return dailyHistoryRepository.existsByHistoryDate(LocalDate.now());
    }
    
    /**
     * Get most viewed daily histories
     */
    public List<DailyHistory> getMostViewedHistories() {
        return dailyHistoryRepository.findMostViewed();
    }
}