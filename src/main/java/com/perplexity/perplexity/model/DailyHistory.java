package com.perplexity.perplexity.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_history")
public class DailyHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "history_date", nullable = false, unique = true)
    private LocalDate historyDate;
    
    @Column(name = "content", columnDefinition = "NVARCHAR(MAX) COLLATE SQL_Latin1_General_CP1_CI_AS", nullable = false)
    private String content;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;
    
    // Constructors
    public DailyHistory() {
        this.createdAt = LocalDateTime.now();
        this.historyDate = LocalDate.now();
    }
    
    public DailyHistory(LocalDate historyDate, String content) {
        this();
        this.historyDate = historyDate;
        this.content = content;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getHistoryDate() {
        return historyDate;
    }
    
    public void setHistoryDate(LocalDate historyDate) {
        this.historyDate = historyDate;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Long getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
    
    public void incrementViewCount() {
        this.viewCount++;
    }
}