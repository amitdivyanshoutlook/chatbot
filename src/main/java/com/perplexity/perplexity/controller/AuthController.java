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

    @Autowired
    private com.perplexity.perplexity.service.UsageService usageService;

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
        
        System.out.println("Login attempt for mobile: " + mobileNumber);
        
        Optional<User> userOpt = userRepository.findByMobileNumber(mobileNumber);
        
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();
            
            // Set session attributes
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFirstName());
            
            // Set session timeout to 30 minutes
            session.setMaxInactiveInterval(1800);
            
            System.out.println("Login successful for user: " + user.getFirstName());
            System.out.println("Session ID: " + session.getId());
            System.out.println("User ID set in session: " + user.getId());
            
            response.put("status", "success");
            response.put("message", "Login successful");
            response.put("userName", user.getFirstName());
            response.put("sessionId", session.getId());
            return ResponseEntity.ok(response);
        }
        
        System.out.println("Login failed for mobile: " + mobileNumber);
        response.put("status", "error");
        response.put("message", "Invalid credentials");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        Map<String, String> response = new HashMap<>();
        
        try {
            System.out.println("Logout request for session: " + session.getId());
            session.invalidate();
            response.put("status", "success");
            response.put("message", "Logged out successfully");
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            response.put("status", "success"); // Still return success even if session was already invalid
            response.put("message", "Logged out successfully");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAuth(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Debug session info
            System.out.println("=== AUTH CHECK ===");
            System.out.println("Session ID: " + session.getId());
            System.out.println("Session is new: " + session.isNew());
            System.out.println("Session creation time: " + new java.util.Date(session.getCreationTime()));
            System.out.println("Session last accessed: " + new java.util.Date(session.getLastAccessedTime()));
            System.out.println("Session max inactive interval: " + session.getMaxInactiveInterval());
            
            Long userId = (Long) session.getAttribute("userId");
            String userName = (String) session.getAttribute("userName");
            
            System.out.println("User ID from session: " + userId);
            System.out.println("User name from session: " + userName);
            
            if (userId != null && userName != null) {
                // Verify user still exists in database
                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isPresent()) {
                    int remainingRequests = usageService.getRemainingRequests(userId);
                    
                    response.put("authenticated", true);
                    response.put("userName", userName);
                    response.put("remainingRequests", remainingRequests);
                    response.put("sessionId", session.getId());
                    
                    System.out.println("Authentication successful - remaining requests: " + remainingRequests);
                } else {
                    // User no longer exists, invalidate session
                    System.out.println("User not found in database, invalidating session");
                    session.invalidate();
                    response.put("authenticated", false);
                    response.put("error", "User not found");
                }
            } else {
                System.out.println("No session data found");
                response.put("authenticated", false);
                response.put("error", "No session data");
            }
            
            System.out.println("=== END AUTH CHECK ===");
            
        } catch (Exception e) {
            System.err.println("Error in checkAuth: " + e.getMessage());
            e.printStackTrace();
            response.put("authenticated", false);
            response.put("error", "Session check failed: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            
            if (userId != null) {
                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("firstName", user.getFirstName());
                    userInfo.put("mobileNumber", user.getMobileNumber());
                    userInfo.put("age", user.getAge());
                    userInfo.put("qualification", user.getQualification());
                    userInfo.put("address", user.getAddress());
                    userInfo.put("createdDate", new java.util.Date()); // Placeholder
                    
                    response.put("success", true);
                    response.put("user", userInfo);
                } else {
                    response.put("success", false);
                    response.put("error", "User not found");
                }
            } else {
                response.put("success", false);
                response.put("error", "Not authenticated");
            }
        } catch (Exception e) {
            System.err.println("Error in getProfile: " + e.getMessage());
            response.put("success", false);
            response.put("error", "Profile fetch failed");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/usage")
    public ResponseEntity<Map<String, Object>> getUsage(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            
            if (userId != null) {
                int remainingRequests = usageService.getRemainingRequests(userId);
                
                response.put("success", true);
                response.put("remainingRequests", remainingRequests);
                response.put("totalRequests", 10);
                response.put("usedRequests", 10 - remainingRequests);
            } else {
                response.put("success", false);
                response.put("error", "Not authenticated");
            }
        } catch (Exception e) {
            System.err.println("Error in getUsage: " + e.getMessage());
            response.put("success", false);
            response.put("error", "Usage fetch failed");
        }
        
        return ResponseEntity.ok(response);
    }
}