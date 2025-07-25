package com.perplexity.perplexity.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_usage")
public class UserUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private LocalDate usageDate;
    
    @Column(nullable = false)
    private Integer requestCount = 0;

    public UserUsage() {}

    public UserUsage(Long userId, LocalDate usageDate, Integer requestCount) {
        this.userId = userId;
        this.usageDate = usageDate;
        this.requestCount = requestCount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public LocalDate getUsageDate() { return usageDate; }
    public void setUsageDate(LocalDate usageDate) { this.usageDate = usageDate; }
    
    public Integer getRequestCount() { return requestCount; }
    public void setRequestCount(Integer requestCount) { this.requestCount = requestCount; }
}