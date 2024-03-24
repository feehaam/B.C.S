package com.bcs.analyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity @Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String word;
    @JsonIgnore
    @ManyToMany
    private List<MCQ> questions;
}
