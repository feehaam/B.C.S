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
            "WHERE (:year IS NULL OR m.year = :year) " +  // Filter by year (null check)
            "AND (:subject IS NULL OR m.subject = :subject) " +  // Filter by subject (null check)
            "AND (:search IS NULL OR (LOWER(m.question) LIKE LOWER(CONCAT('%', :search, '%')))) " + // Filter by search term (case-insensitive)
            "AND (:tags IS NULL OR t.word IN (:tags)) " + // Filter by tags (empty array check)
            "GROUP BY m.id " +  // Group by MCQ id to avoid duplicates from tags
            "ORDER BY m.similarity DESC")  // Sort by similarity (descending)
    List<MCQ> findAllByFilters(
            @Param("year") Integer year,
            @Param("subject") String subject,
            @Param("search") String search,
            @Param("tags") List<String> tags);

    @Query("SELECT m FROM MCQ m " +
            "LEFT JOIN m.tags t " +  // Left join with Tag for filtering by tags
            "WHERE (:year IS NULL OR m.year = :year) " +  // Filter by year (null check)
            "AND (:subject IS NULL OR m.subject = :subject) " +  // Filter by subject (null check)
            "AND (:search IS NULL OR (LOWER(m.question) LIKE LOWER(CONCAT('%', :search, '%')))) " + // Filter by search term (case-insensitive)
            "AND (:tags IS NULL OR t.word IN (:tags)) " + // Filter by tags (empty array check)
            "GROUP BY m.id " +  // Group by MCQ id to avoid duplicates from tags
            "ORDER BY m.similarity DESC")  // Sort by similarity (descending)
    List<MCQ> findAllByFiltersV2(
            @Param("year") Integer year,
            @Param("subject") String subject,
            @Param("search") String search,
            @Param("tags") List<String> tags);

    @Query("SELECT m.subject, t " +
            "FROM MCQ m " +
            "LEFT JOIN m.tags t " +
            "WHERE m.subject IS NOT NULL AND t IS NOT NULL")
    List<Object[]> findAllWithSubjectAndTags();
}
