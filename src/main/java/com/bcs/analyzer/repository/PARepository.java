package com.bcs.analyzer.repository;

import com.bcs.analyzer.model.PendingAnalyzer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PARepository extends JpaRepository<PendingAnalyzer, Integer> {
}
