package com.perplexity.perplexity.repository;

import com.perplexity.perplexity.model.CareerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerOptionRepository extends JpaRepository<CareerOption, Long> {
    
    @Query("SELECT c FROM CareerOption c WHERE LOWER(c.qualification) LIKE LOWER(CONCAT('%', :qualification, '%'))")
    List<CareerOption> findByQualificationContainingIgnoreCase(@Param("qualification") String qualification);
    
    @Query("SELECT c FROM CareerOption c WHERE LOWER(c.qualification) = LOWER(:qualification)")
    List<CareerOption> findByQualificationIgnoreCase(@Param("qualification") String qualification);
    
    @Query("SELECT c FROM CareerOption c WHERE c.category = :category")
    List<CareerOption> findByCategory(@Param("category") String category);
    
    @Query("SELECT DISTINCT c.qualification FROM CareerOption c ORDER BY c.qualification")
    List<String> findAllQualifications();
}