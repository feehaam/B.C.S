package com.bcs.analyzer.repository;

import com.bcs.analyzer.model.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanRepository extends JpaRepository<Ban, Integer> {

}
