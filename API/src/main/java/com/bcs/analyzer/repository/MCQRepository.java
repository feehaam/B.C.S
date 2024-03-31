package com.bcs.analyzer.repository;

import com.bcs.analyzer.model.MCQ;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MCQRepository extends MongoRepository<MCQ, Integer> {
    @Query("SELECT m.subject, t " +
            "FROM MCQ m " +
            "LEFT JOIN m.tags t " +
            "WHERE m.subject IS NOT NULL AND t IS NOT NULL")
    List<Object[]> findAllWithSubjectAndTags();
}
