package com.perplexity.perplexity.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SessionDebugFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Debug session info for auth-related requests
        if (httpRequest.getRequestURI().contains("/api/auth/")) {
            System.out.println("=== SESSION DEBUG FILTER ===");
            System.out.println("Request URI: " + httpRequest.getRequestURI());
            System.out.println("Request Method: " + httpRequest.getMethod());
            System.out.println("Session ID from request: " + httpRequest.getRequestedSessionId());
            System.out.println("Session ID from cookie: " + (httpRequest.getSession(false) != null ? httpRequest.getSession(false).getId() : "null"));
            
            HttpSession session = httpRequest.getSession(false);
            if (session != null) {
                System.out.println("Session exists: " + session.getId());
                System.out.println("Session is new: " + session.isNew());
                System.out.println("User ID in session: " + session.getAttribute("userId"));
                System.out.println("User name in session: " + session.getAttribute("userName"));
            } else {
                System.out.println("No session found");
            }
            
            // Check cookies
            if (httpRequest.getCookies() != null) {
                System.out.println("Cookies:");
                for (var cookie : httpRequest.getCookies()) {
                    System.out.println("  " + cookie.getName() + " = " + cookie.getValue());
                }
            } else {
                System.out.println("No cookies found");
            }
            
            System.out.println("=== END SESSION DEBUG ===");
        }
        
        chain.doFilter(request, response);
    }
}