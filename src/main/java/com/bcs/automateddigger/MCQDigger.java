package com.bcs.automateddigger;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MCQDigger {

    public static Object getMCQ(WebElement questionElement, String subject, int year) {
        try{
            String question = getQuestion(questionElement);
            List<WebElement> optionsElements = questionElement.findElements(By.tagName("span"));
            String optionA = optionsElements.get(1).getText();
            String optionB = optionsElements.get(3).getText();
            String optionC = optionsElements.get(5).getText();
            String optionD = optionsElements.get(7).getText();
            int answer = getAnswer(optionsElements);
            String explanation = getExplanation(questionElement);

            return MCQ.builder().question(question).optionA(optionA).optionB(optionB).optionC(optionC).optionD(optionD)
                    .answer(answer).explanation(explanation).subject(subject).year(year).build();
        }
        catch (Exception e){
            return e;
        }
    }

    private static String getExplanation(WebElement questionElement) {
        List<WebElement> explanations = questionElement.findElements(By.cssSelector("div.solve-note"));
        String explanation = explanations.getFirst().getText();
        if(explanation.contains("ব্যাখ্যা:"))
            explanation = explanation.replaceFirst("ব্যাখ্যা:", "");
        while(explanation.contains("\n")){
            explanation = explanation.replace("\n", " ");
        }
        while (explanation.contains("  ")){
            explanation = explanation.replaceAll("  ", " ");
        }
        if (explanation.isEmpty()) {
            try{
                List<WebElement> images = questionElement.findElements(By.tagName("img"));
                if (!images.isEmpty()) {
                    return images.get(0).getAttribute("src");
                }
            }
            catch (Exception e){
                return null;
            }
        }
        return explanation;
    }

    private static int getAnswer(List<WebElement> optionsElements) {
        int answer = 0;
        for (WebElement optionElement : optionsElements) {
            if (optionElement.getAttribute("class").contains("empty")) {
                switch (optionElement.getText()){
                    case "A" -> answer = 1;
                    case "B" -> answer = 2;
                    case "C" -> answer = 3;
                    case "D" -> answer = 4;
                }
                break;
            }
        }
        return answer;
    }

    private static String getQuestion(WebElement questionElement){
        List<WebElement> questions = questionElement.findElements(By.cssSelector("div.p-2"));
        String question = questions.getFirst().getText();
        if(question.contains("\n")){
            question = question.substring(question.indexOf("\n") + 1, question.length());
        }
        return question;
    }
}