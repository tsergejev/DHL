package org.dhl.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.sql.Driver;
import java.time.Duration;

public class WebDriverTestClass {
    //protected RemoteWebDriver driver;
    protected WebDriver driver;

    @BeforeSuite
    /*public void setDriverPath() {
        System.setProperty("webdriver.chrome.driver", "C:\\tools\\chromedriver.exe");
    }

     */
    public void setDriver() {
        driver = new FirefoxDriver();
    }

    @AfterMethod
    public void reset() throws InterruptedException {
        Thread.sleep(5000);
        this.driver.quit();
    }

    public WebElement waitForElement(By by, RemoteWebDriver driver) {
        waitForJQueriesDone(driver);
        waitForPageToLoad(driver);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60), Duration.ofSeconds(2));
        ExpectedCondition<WebElement> expectedCondition = ExpectedConditions.visibilityOfElementLocated(by);
        return wait.until(expectedCondition);
    }

    private void waitForPageToLoad(RemoteWebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        ExpectedCondition<Boolean> expectedCondition = (d) -> driver.executeScript("return document.readyState;").equals("complete");
        wait.until(expectedCondition);
    }

    private void waitForJQueriesDone(RemoteWebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        ExpectedCondition<Boolean> expectedCondition = (d) -> {
            if ((Boolean) driver.executeScript("return window.jQuery === undefined;")) {
                return true;
            }
            try {
                return (Boolean) driver.executeScript("return jQuery.active === 0;");
            } catch (JavascriptException e) {
                return false;
            }
        };
        wait.until(expectedCondition);
    }
}

