package com.bcs.analyzer.repository;

import com.bcs.analyzer.model.PendingAnalyzer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PARepository extends MongoRepository<PendingAnalyzer, Integer> {
    List<PendingAnalyzer> findByTargetIdAndTargetType(int targetId, int targetType);
    Page<PendingAnalyzer> findByTargetType(int targetType, Pageable pageable);
    List<PendingAnalyzer> findByPriority(int priority);
}
