package com.bcs.analyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class Tag {
    @Id
    private Integer id;
    private String word;
    @JsonIgnore
    private List<MCQ> questions;
}
