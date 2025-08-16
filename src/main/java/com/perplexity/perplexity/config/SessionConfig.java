package com.perplexity.perplexity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionCookieConfig;

@Configuration
public class SessionConfig {

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
                sessionCookieConfig.setName("JSESSIONID");
                sessionCookieConfig.setMaxAge(1800); // 30 minutes
                sessionCookieConfig.setHttpOnly(true);
                sessionCookieConfig.setSecure(false); // Set to false for development
                sessionCookieConfig.setPath("/");
            }
        };
    }
}