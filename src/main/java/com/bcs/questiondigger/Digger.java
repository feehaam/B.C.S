package com.bcs.questiondigger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Digger {
    static final List<MCQ> questionSet = new ArrayList<>();
    static Integer currentQuestionBcsNo;
    public static void main(String[] args) throws JsonProcessingException {
        Scanner sc = new Scanner(System.in);
        currentQuestionBcsNo = sc.nextInt();
        String html = "";
        while (sc.hasNextLine()){
            String input = sc.nextLine();
            if(input.equals("./")) break;
            html += input;
        }
        List<String> mcqList = tagBasedExtractor(html, "fieldset");
        int i = 1;
        for (String mcq: mcqList){
            extractMcq(mcq, i++);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(questionSet);
        FFiles.create("F://bcs-questions/" + currentQuestionBcsNo + ".json");
        FFiles.changeData("F://bcs-questions/" + currentQuestionBcsNo + ".json", json);
    }

    static void extractMcq(String mcq, int iteration){
        try{
            String question = getQuestion(mcq);
            List<String> options = getOptions(mcq);
            String correctAns = getCorrectAnswer(mcq);

            questionSet.add(new MCQ(question, options, correctAns, currentQuestionBcsNo));
        }
        catch (Exception e){
            System.out.println("Error in iteration " + iteration);
        }
    }

    static String getQuestion(String mcq){
        String question = tagBasedExtractor(mcq, "h5").get(0);
        return question.substring(question.indexOf(".") + 2);
    }

    static List<String> getOptions(String mcq){
        List<String> options = tagBasedExtractor(mcq, "strong");
        List<String> result = new ArrayList<>();
        for(int i=1; i<options.size(); i++) {
            String option = options.get(i);
            option = option.substring(4);
            result.add(option);
        }
        return result;
    }

    static String getCorrectAnswer(String mcq){
        String ca = mcq.substring(mcq.indexOf("<font color=\"green\" size=\"+2\">"));
        ca = ca.substring(0, ca.indexOf("</font>"));
        ca = ca.replace("<font color=\"green\" size=\"+2\">", "");
        return ca;
    }

    static List<String> tagBasedExtractor(String text, String tag){
        String beginningTag = "<" + tag + ">";
        String endingTag = "</" + tag + ">";
        List<String> results = new ArrayList<>();
        while(text.contains(beginningTag)) {
            text = text.substring(text.indexOf(beginningTag));
            String outerHtml = text.substring(0, text.indexOf(endingTag) + endingTag.length());
            String innterHtml = outerHtml.replace(beginningTag, "").replace(endingTag, "");
            results.add(innterHtml);
            text = text.substring(text.indexOf(endingTag) + endingTag.length());
        }
        return results;
    }
}
