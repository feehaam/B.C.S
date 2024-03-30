package com.bcs.analyzer.repository;

import com.bcs.analyzer.model.Ban;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BanRepository extends MongoRepository<Ban, Integer> {

}
