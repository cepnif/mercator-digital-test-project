package com.saucedemo.pages;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * BasePage is an abstract class that provides shared WebDriver functionality and common utilities
 * for all page objects. It is not intended to be instantiated directly.
 * This class serves as a reusable foundation for page classes, enforcing consistent interaction
 * patterns and promoting maintainable automation design.
 */
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Duration DEFAULT_WAIT_TIME = Duration.ofSeconds(10);

    /**
     * Constructor to initialize BasePage with a WebDriver instance.
     *
     * @param driver the WebDriver instance to be used for browser interaction
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_WAIT_TIME);
    }

    /**
     * Waits for a WebElement to become visible and returns it.
     *
     * @param locator By locator for the desired element
     * @return WebElement that is visible on the page
     * @throws NoSuchElementException if element not found within wait time
     */
    protected WebElement find(By locator) {
        try {
            logger.debug("Waiting for visibility of element located by: {}", locator);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            logger.error("Element not visible after {} seconds: {}", DEFAULT_WAIT_TIME.getSeconds(), locator);
            throw new NoSuchElementException("Element not found or visible: " + locator, e);
        }
    }

    /**
     * Clicks on an element after ensuring it is clickable.
     *
     * @param locator By locator of the element to click
     * @return void
     */
    protected void click(By locator) {
        try {
            logger.debug("Waiting for element to be clickable: {}", locator);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            logger.info("Clicked element: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to click element: {}", locator, e);
            throw e;
        }
    }

    /**
     * Sends text to a specified input field after clearing it.
     *
     * @param locator By locator of the input field
     * @param text    String text value to send
     * @return void
     */
    protected void setText(By locator, String text) {
        try {
            WebElement element = find(locator);
            logger.info("Setting text '{}' to element: {}", text, locator);
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            logger.error("Failed to send text '{}' to element: {}", text, locator, e);
            throw e;
        }
    }

    /**
     * Retrieves visible text from a WebElement.
     *
     * @param locator By locator of the element
     * @return String text content of the element
     */
    protected String getText(By locator) {
        try {
            WebElement element = find(locator);
            String text = element.getText();
            logger.info("Extracted text '{}' from element: {}", text, locator);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {}", locator, e);
            throw e;
        }
    }

    /**
     * Checks if a WebElement is currently displayed on the page.
     *
     * @param locator By locator of the element
     * @return boolean true if element is displayed, false otherwise
     */
    protected boolean isDisplayed(By locator) {
        try {
            boolean visible = find(locator).isDisplayed();
            logger.info("Element is displayed: {}", locator);
            return visible;
        } catch (Exception e) {
            logger.warn("Element not displayed or not found: {}", locator);
            return false;
        }
    }

    /**
     * Refreshes the current page in the browser.
     *
     * @return void
     */
    protected void refreshPage() {
        logger.info("Refreshing the current page.");
        driver.navigate().refresh();
    }

    /**
     * Navigates to the previous page in browser history.
     *
     * @return void
     */
    protected void navigateBack() {
        logger.info("Navigating back to the previous page.");
        driver.navigate().back();
    }

    /**
     * Navigates forward to the next page in browser history.
     *
     * @return void
     */
    protected void navigateForward() {
        logger.info("Navigating forward to the next page.");
        driver.navigate().forward();
    }

    /**
     * Scrolls to the specified WebElement using JavaScriptExecutor.
     *
     * @param element WebElement to scroll to
     * @return void
     */
    protected void scrollToElement(WebElement element) {
        logger.info("Scrolling to element: {}", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Uploads a file by sending its path to an input[type='file'] field.
     *
     * @param locator  By locator of the file input element
     * @param filePath String full path of the file to upload
     * @return void
     */
    protected void uploadFile(By locator, String filePath) {
        try {
            logger.info("Uploading file: {}", filePath);
            WebElement uploadElement = find(locator);
            uploadElement.sendKeys(filePath);
        } catch (Exception e) {
            logger.error("File upload failed: {}", filePath, e);
            throw e;
        }
    }
}
