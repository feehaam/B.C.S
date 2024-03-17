package com.bcs.analyzer.model;

import lombok.*;

import java.util.List;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class MCQDTO {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int answer;
    private String subject;
    private int year;
    private List<String> tagWords;
    private List<String> bans;
    public void setTagWords(List<String> words){
        words.forEach(word -> tagWords.add(word.toLowerCase()));
    }
}