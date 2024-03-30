package com.bcs.analyzer.repository;

import com.bcs.analyzer.model.PendingAnalyzer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PARepository extends JpaRepository<PendingAnalyzer, Integer> {
    List<PendingAnalyzer> findByTargetIdAndTargetType(int targetId, int targetType);
    Page<PendingAnalyzer> findByTargetType(int targetType, Pageable pageable);
    List<PendingAnalyzer> findByPriority(int priority);
}
