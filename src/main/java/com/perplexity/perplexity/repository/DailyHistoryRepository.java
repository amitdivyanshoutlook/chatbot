package com.perplexity.perplexity.repository;

import com.perplexity.perplexity.model.DailyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyHistoryRepository extends JpaRepository<DailyHistory, Long> {
    
    // Find daily history by date
    Optional<DailyHistory> findByHistoryDate(LocalDate date);
    
    // Find recent daily histories (last 30 days)
    @Query("SELECT dh FROM DailyHistory dh WHERE dh.historyDate >= :fromDate ORDER BY dh.historyDate DESC")
    List<DailyHistory> findRecentDailyHistories(@Param("fromDate") LocalDate fromDate);
    
    // Increment view count
    @Modifying
    @Transactional
    @Query("UPDATE DailyHistory dh SET dh.viewCount = dh.viewCount + 1 WHERE dh.id = :id")
    void incrementViewCount(@Param("id") Long id);
    
    // Check if today's history exists
    @Query("SELECT COUNT(dh) > 0 FROM DailyHistory dh WHERE dh.historyDate = :date")
    boolean existsByHistoryDate(@Param("date") LocalDate date);
    
    // Get most viewed daily histories
    @Query("SELECT dh FROM DailyHistory dh ORDER BY dh.viewCount DESC")
    List<DailyHistory> findMostViewed();
}