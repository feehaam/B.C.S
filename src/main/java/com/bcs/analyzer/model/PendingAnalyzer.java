package com.bcs.analyzer.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class PendingAnalyzer {
    @Id
    private Integer id;
    private int targetId;
    private int targetType;
    private int priority;
}
