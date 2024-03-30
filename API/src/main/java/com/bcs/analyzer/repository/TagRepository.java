package com.bcs.analyzer.repository;

import com.bcs.analyzer.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends MongoRepository<Tag, Integer> {
    @Query("SELECT t FROM Tag t WHERE t.id IN :ids")
    List<Tag> findAllByIds(@Param("ids") List<Integer> ids);

    @Query("SELECT t FROM Tag t WHERE t.word IN :words")
    List<Tag> findAllByWords(@Param("words") List<String> words);
}
