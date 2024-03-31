package com.bcs.automateddigger;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor @Builder
public class MCQ {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int answer;
    private String explanation;
    private String subject;
    private int year;

    @Override
    public String toString(){
        return question + "\n" + optionA + "\n" + optionB + "\n"
                + optionC + "\n" + optionD + "\n" + subject + " | "
                + answer + " | " + year + "\n" + explanation
                + "\n--------------------------------\n";
    }
}