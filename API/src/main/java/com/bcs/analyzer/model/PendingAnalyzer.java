package com.bcs.analyzer.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class PendingAnalyzer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int targetId;
    private int targetType;
    private int priority;
}
