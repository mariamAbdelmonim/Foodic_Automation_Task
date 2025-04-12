package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class Waits {

    // Default wait times for explicit waits and polling intervals
    private static final long DEFAULT_EXPLICIT_WAIT = 20;
    private static final long DEFAULT_POLLING_INTERVAL = 500;

    // WebDriver instance
    private final WebDriver driver;

    // Constructor to initialize the WebDriver instance
    public Waits() {
        this.driver = DriverManager.getDriver();
    }

    // Wait for an element to be visible (with default timeout)
    public void waitForElementToBeVisible(WebElement element) {
        waitForElementToBeVisible(element, Duration.ofSeconds(DEFAULT_EXPLICIT_WAIT));
    }

    // Wait for an element to be visible (with custom timeout)
    public void waitForElementToBeVisible(WebElement element, Duration timeout) {
        new WebDriverWait(driver, timeout)
                .pollingEvery(Duration.ofMillis(DEFAULT_POLLING_INTERVAL))  // Polling interval for checking visibility
                .ignoring(StaleElementReferenceException.class)  // Ignore StaleElementReferenceException during polling
                .until(ExpectedConditions.visibilityOf(element));  // Wait until element is visible
    }

    // Wait for an element to be clickable (with default timeout)
    public void waitForElementToBeClickable(WebElement element) {
        waitForElementToBeClickable(element, Duration.ofSeconds(DEFAULT_EXPLICIT_WAIT));
    }

    // Wait for an element to be clickable (with custom timeout)
    public void waitForElementToBeClickable(WebElement element, Duration timeout) {
        new WebDriverWait(driver, timeout)
                .pollingEvery(Duration.ofMillis(DEFAULT_POLLING_INTERVAL))  // Polling interval for checking clickability
                .ignoring(StaleElementReferenceException.class)  // Ignore StaleElementReferenceException during polling
                .until(ExpectedConditions.elementToBeClickable(element));  // Wait until element is clickable
    }

    // Wait for the entire page to load completely
    public void waitForPageToLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_EXPLICIT_WAIT))
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    // Wait for either of the two elements to be visible
    public WebElement waitForEither(Duration timeout, WebElement firstElement, WebElement secondElement) {
        return new WebDriverWait(driver, timeout)
                .until(driver -> {
                    try {
                        if (firstElement.isDisplayed()) return firstElement;  // If first element is displayed, return it
                        if (secondElement.isDisplayed()) return secondElement;  // If second element is displayed, return it
                    } catch (Exception e) {
                        // Ignore exceptions if element is not found or not visible
                    }
                    return null;  // Return null if neither element is visible
                });
    }

    // Sleep for a specified duration (in milliseconds)
    public void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());  // Pause the thread for the specified duration
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restore interrupted status
            throw new RuntimeException("Sleep was interrupted", e);  // Throw runtime exception if interrupted
        }
    }
}
