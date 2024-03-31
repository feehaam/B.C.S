package com.bcs.analyzer.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class UnifiedDTO {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int answer;
    private String explanation;
    private String subject;
    private int year;
    private List<String> tagWords;
    private List<String> bans;
    public void setTagWords(List<String> words){
        tagWords = new ArrayList<>();
        words.forEach(word -> tagWords.add(word.toLowerCase()));
    }
    private void setBans(List<String> words){
        bans = new ArrayList<>();
        words.forEach(word -> bans.add(word.toLowerCase()));
    }
}