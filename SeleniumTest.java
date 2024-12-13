package org.dhl.example;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import java.time.Duration;

public class SeleniumTest extends WebDriverTestClass{

    private RemoteWebDriver driver;
    private WebElement originCountrySelect;
    private WebElement destinationCountrySelect;
    private WebElement originPostcodeInput;
    private WebElement destinationPostcodeInput;
    private WebElement cookiesAcceptButton;
    private WebElement calculateButton;

    String baseUrl = "https://www.dhl.com/se-en/home/freight/tools/european-road-freight-transit-time-calculator.html";
        @BeforeSuite
        public void setup() {
            this.driver = new FirefoxDriver();
            // launch Fire fox and direct it to the Base URL
            this.driver.get(baseUrl);
            this.driver.manage().window().maximize();
            // wait until element present
            this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            // wait until page is loaded
            this.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
            // wait until js function ends
            this.driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            this.originCountrySelect = waitForElement(By.id("origin-country"),this.driver);
            this.destinationCountrySelect = waitForElement(By.id("destination-country"),this.driver);
            this.originPostcodeInput = waitForElement(By.id("origin-postcode"),this.driver);
            this.destinationPostcodeInput = waitForElement(By.id("destination-postcode"),this.driver);
            this.cookiesAcceptButton = waitForElement(By.id("onetrust-accept-btn-handler"), driver);
            this.calculateButton = waitForElement(By.xpath("//*[@class='l-grid--center-s l-grid--w-100pc-s l-grid--w-100pc-m js--freightcalc-se--input-submit base-button c-swe-leadtime-button']"),this.driver);

            this.cookiesAcceptButton.click();
        }

        @Test
        public void testHappyPath() {
            originCountrySelect.sendKeys("Slovakia");
            originPostcodeInput.sendKeys("01701");
            destinationPostcodeInput.sendKeys("26234");
            calculateButton.click();
        }

         @Test
         public void wrongOriginPostcode() {
             destinationPostcodeInput.sendKeys("26234");
             originPostcodeInput.sendKeys("a");
             this.calculateButton = waitForElement(By.xpath("//*[@class='l-grid--center-s l-grid--w-100pc-s l-grid--w-100pc-m js--freightcalc-se--input-submit base-button c-swe-leadtime-button']"),this.driver);
             calculateButton.click();
             WebElement originPostcodeError = waitForElement(By.xpath("//*[@class='l-grid c-calculator--text-align-left l-grid--w-100pc-s js--origin-zip-error c-calculator--zip-error-message c-swe-leadtime-zip-error-message']"),this.driver);
             String actualErrorCode = originPostcodeError.getText();
             Assert.assertEquals(actualErrorCode,"Correct postal code (e.g. no post box)*");
         }

        @AfterClass
        public void quit(){
            this.driver.quit();
        }
    }

