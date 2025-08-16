package com.perplexity.perplexity.service;

import com.perplexity.perplexity.model.UserUsage;
import com.perplexity.perplexity.repository.UserUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UsageService {
    
    private static final int DAILY_LIMIT = 10;
    
    @Autowired
    private UserUsageRepository userUsageRepository;
    
    public boolean canMakeRequest(Long userId) {
     /*   LocalDate today = LocalDate.now();
        Optional<UserUsage> usageOpt = userUsageRepository.findByUserIdAndUsageDate(userId, today);
        if(usageOpt.isPresent()){
            if(usageOpt.get().getId() == 1L){
                // Special case for user with ID 1, always allow requests
                return true;
            }
        }
        if (usageOpt.isPresent()) {
            return usageOpt.get().getRequestCount() < DAILY_LIMIT;
        }*/
        
        return true; // First request of the day
    }
    
    public void incrementUsage(Long userId) {
        LocalDate today = LocalDate.now();
        Optional<UserUsage> usageOpt = userUsageRepository.findByUserIdAndUsageDate(userId, today);
        
        if (usageOpt.isPresent()) {
            UserUsage usage = usageOpt.get();
            usage.setRequestCount(usage.getRequestCount() + 1);
            userUsageRepository.save(usage);
        } else {
            UserUsage newUsage = new UserUsage(userId, today, 1);
            userUsageRepository.save(newUsage);
        }
    }
    
    public int getRemainingRequests(Long userId) {
        LocalDate today = LocalDate.now();
        Optional<UserUsage> usageOpt = userUsageRepository.findByUserIdAndUsageDate(userId, today);
        
        if (usageOpt.isPresent()) {
            return DAILY_LIMIT - usageOpt.get().getRequestCount();
        }
        
        return DAILY_LIMIT;
    }
}