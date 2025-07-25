package com.perplexity.perplexity.controller;

import com.perplexity.perplexity.model.User;
import com.perplexity.perplexity.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        
        if (userRepository.existsByMobileNumber(user.getMobileNumber())) {
            response.put("status", "error");
            response.put("message", "Mobile number already registered");
            return ResponseEntity.badRequest().body(response);
        }
        
        userRepository.save(user);
        response.put("status", "success");
        response.put("message", "Registration successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginData, HttpSession session) {
        Map<String, String> response = new HashMap<>();
        String mobileNumber = loginData.get("mobileNumber");
        String password = loginData.get("password");
        
        Optional<User> userOpt = userRepository.findByMobileNumber(mobileNumber);
        
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            session.setAttribute("userId", userOpt.get().getId());
            session.setAttribute("userName", userOpt.get().getFirstName());
            response.put("status", "success");
            response.put("message", "Login successful");
            response.put("userName", userOpt.get().getFirstName());
            return ResponseEntity.ok(response);
        }
        
        response.put("status", "error");
        response.put("message", "Invalid credentials");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        Map<String, String> response = new HashMap<>();
        session.invalidate();
        response.put("status", "success");
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAuth(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId != null) {
            response.put("authenticated", true);
            response.put("userName", session.getAttribute("userName"));
        } else {
            response.put("authenticated", false);
        }
        
        return ResponseEntity.ok(response);
    }
}