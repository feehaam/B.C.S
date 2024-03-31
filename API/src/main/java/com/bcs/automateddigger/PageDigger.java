package com.bcs.automateddigger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;

public class PageDigger {
    public static List<Object> dig(String si, int year) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Driver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://uttoron.academy/QuestionBank/Question/" + si + "-BCS");

        WebElement subjectSelectBox = driver.findElement(By.id("Subject"));
        List<WebElement> options = subjectSelectBox.findElements(By.tagName("option"));

        // Skip the first option
        List<Object> results = new ArrayList<>();
        int optionIndex = 1;
        for (; optionIndex < options.size(); optionIndex++) {
            WebElement option = options.get(optionIndex);
            WebElement displayAnswerButton = null;
            List<WebElement> singleQuestions = new ArrayList<>();
            int sleepTime = 100;
            while (displayAnswerButton == null || singleQuestions.isEmpty()){
                boolean noQues = false;
                try{
                    option.click();
                    Thread.sleep(sleepTime);
                    List<WebElement> dangers = driver.findElements(By.cssSelector("h1.text-danger"));
                    for(WebElement danger: dangers){
                        if(danger.getText().equals("Question Not Found")){
                            noQues = true;
                            break;
                        }
                    }
                    if(noQues) {
                        break;
                    }
                    displayAnswerButton = driver.findElement(By.cssSelector("button.btn.btn-display-answer.px-0"));
                    displayAnswerButton.click();
                    Thread.sleep(1000);
                    singleQuestions = driver.findElements(By.cssSelector("div.single-question"));
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
                if(displayAnswerButton == null || singleQuestions.isEmpty()){
                    sleepTime += 100;
                }
            }
            for (WebElement question : singleQuestions) {
                results.add(MCQDigger.getMCQ(question, option.getText(), year));
            }
        }

        driver.close();
        driver.quit();
        return results;
    }
}
