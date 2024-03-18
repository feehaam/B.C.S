package com.bcs.automateddigger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class FirstScript {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Driver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://uttoron.academy/QuestionBank/Question/45th-BCS");

        WebElement subjectSelectBox = driver.findElement(By.id("Subject"));
        List<WebElement> options = subjectSelectBox.findElements(By.tagName("option"));

        // Skip the first option
        int optionIndex = 1;

        for (WebElement option : options.subList(1, options.size())) {  // Start from second option
            option.click();
            System.out.println(option.getText() + "-----------------------> OPTION CLICKED!");
            Thread.sleep(4000);
            WebElement displayAnswerButton = driver.findElement(By.cssSelector("button.btn.btn-display-answer.px-0"));
            displayAnswerButton.click();
            System.out.println("-----------------------> ANS BTN CLICKED!");
            Thread.sleep(4000);
            WebElement popupCloseButton = driver.findElement(By.cssSelector("span.position-absolute.btn-close-report"));
            popupCloseButton.click();
            System.out.println("-----------------------> POPUP CLOSE CLICKED!");
            List<WebElement> singleQuestions = driver.findElements(By.cssSelector("div.single-question"));
            for (WebElement question : singleQuestions) {
                System.out.println(question.getText());
                Thread.sleep(500);
            }
            optionIndex++;
        }

        driver.quit();
    }
}