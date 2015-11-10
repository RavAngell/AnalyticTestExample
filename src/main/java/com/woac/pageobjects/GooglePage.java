package com.woac.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Google page object model - contains base page elements and interactions with them.
 */
public class GooglePage {
    public static final String URL = "http://google.com", NAME = "Google";

    private By searchField = By.cssSelector("[action*='search'] input[aria-label]");
    private WebDriver driver;

    public GooglePage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Sends specified @term inside search box on google page and presses 'Enter' key.
     * @param term - specifies term to be searched
     */
    public void searchFor(String term) {
        System.out.println("Locating search field element"); //Note: this method could be split in 3 separate ones
        WebElement field = driver.findElement(searchField);

        System.out.println("Typing the search term in google search box: " + term);
        field.sendKeys(term);

        System.out.println("Pressing [Enter] button");
        field.sendKeys(Keys.RETURN);
    }

    /**
     * Navigates to Google page.
     * @return {@code GooglePage}
     */
    public GooglePage navigate() {
        System.out.println("Navigating to page by URL: " + URL);
        driver.get(URL);
        return this;
    }
}
