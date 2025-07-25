package com.perplexity.perplexity.repository;

import com.perplexity.perplexity.model.UserUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserUsageRepository extends JpaRepository<UserUsage, Long> {
    Optional<UserUsage> findByUserIdAndUsageDate(Long userId, LocalDate usageDate);
}