package com.bcs.analyzer.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class Ban {
    @Id
    private Integer id;
    private String word;
}
