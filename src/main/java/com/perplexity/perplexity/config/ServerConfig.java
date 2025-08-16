package com.perplexity.perplexity.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Server configuration to handle HTTP/HTTPS setup properly
 */
@Configuration
public class ServerConfig {

    /**
     * Customize the Tomcat server to handle HTTP requests properly
     * This prevents the "Invalid character found in method name" error
     * that occurs when clients try to connect with HTTP to an HTTPS port
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer() {
        return factory -> {
            // Ensure proper HTTP handling
            factory.addConnectorCustomizers(connector -> {
                // Set proper protocol handling
                connector.setProperty("relaxedPathChars", "[]|");
                connector.setProperty("relaxedQueryChars", "[]|{}^&#x5c;&#x60;&quot;&lt;&gt;");
                
                // Improve error handling for malformed requests
                connector.setProperty("rejectIllegalHeader", "false");
            });
        };
    }

}