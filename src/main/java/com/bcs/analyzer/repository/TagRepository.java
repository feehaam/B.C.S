package com.bcs.analyzer.repository;

import com.bcs.analyzer.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Query("SELECT t FROM Tag t WHERE t.id IN :ids")
    List<Tag> findAllByIds(@Param("ids") List<Integer> ids);

    @Query("SELECT t FROM Tag t WHERE t.word IN :words")
    List<Tag> findAllByWords(@Param("words") List<String> words);
}
