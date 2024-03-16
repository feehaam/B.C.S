package com.bcs.questiondigger;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MCQ {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private Integer answer;
    private Integer year;

    public MCQ(String question, List<String> options, String correctAnswer, Integer bcsNo){
        this.question = question;
        optionA = options.get(0);
        optionB = options.get(1);
        optionC = options.get(2);
        optionD = options.get(3);
        for(int i = 0; i < options.size(); i++){
            if(options.get(i).equals(correctAnswer)){
                answer = i + 1;
                break;
            }
        }
        this.year = 1978 + bcsNo;
    }
}
