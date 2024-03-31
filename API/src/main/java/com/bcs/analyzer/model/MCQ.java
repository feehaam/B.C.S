package com.bcs.analyzer.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class MCQ {
    @Id
    private Integer id;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int answer;
    private String explanation;
    private String subject;
    private int year;
    private int similarity;
    @DBRef
    private List<Tag> tags;
    private LocalDateTime updateTime;
}
