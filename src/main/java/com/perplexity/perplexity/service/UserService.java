package com.perplexity.perplexity.service;

import com.perplexity.perplexity.model.User;
import com.perplexity.perplexity.model.UserUsage;
import com.perplexity.perplexity.repository.UserRepository;
import com.perplexity.perplexity.repository.UserUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    

    
    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    

    

}