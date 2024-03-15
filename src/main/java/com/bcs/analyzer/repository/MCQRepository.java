package com.bcs.analyzer.repository;

import com.bcs.analyzer.model.MCQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MCQRepository extends JpaRepository<MCQ, Integer> {
    @Query("SELECT m FROM MCQ m " +
            "LEFT JOIN m.tags t " +  // Left join with Tag for filtering by tags
            "WHERE (m.year = :year OR :year IS NULL) " +  // Filter by year (null check)
            "AND (m.subject = :subject OR :subject IS NULL) " +  // Filter by subject (null check)
            "AND (LOWER(m.question) LIKE LOWER(CONCAT('%', :search, '%'))) " + // Filter by search term (case-insensitive)
            "AND (t.word IN (:tags) OR SIZE(:tags) = 0) " + // Filter by tags (empty array check)
            "GROUP BY m.id " +  // Group by MCQ id to avoid duplicates from tags
            "ORDER BY m.similarity DESC")  // Sort by similarity (descending)
    List<MCQ> findAllByFilters(
            @Param("year") Integer year,
            @Param("subject") String subject,
            @Param("search") String search,
            @Param("tags") List<String> tags);
}
