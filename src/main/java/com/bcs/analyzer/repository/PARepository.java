package com.bcs.analyzer.repository;

import com.bcs.analyzer.model.PendingAnalyzer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PARepository extends JpaRepository<PendingAnalyzer, Integer> {
    PendingAnalyzer findByTargetIdAndTargetType(int targetId, int targetType);
    Page<PendingAnalyzer> findByTargetType(int targetType, Pageable pageable);
}
