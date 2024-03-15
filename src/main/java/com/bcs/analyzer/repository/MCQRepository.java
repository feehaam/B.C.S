package com.bcs.analyzer.repository;

import com.bcs.analyzer.model.MCQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MCQRepository extends JpaRepository<MCQ, Integer> {

}
