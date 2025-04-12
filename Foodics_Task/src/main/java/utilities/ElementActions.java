package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;

public class ElementActions {

    // WebDriver instance and Waits helper class to handle waits for elements
    private final WebDriver driver;
    private final Waits waits;

    // Constructor to initialize WebDriver and Waits instance
    public ElementActions() {
        this.driver = DriverManager.getDriver();  // Get WebDriver from DriverManager
        this.waits = new Waits();  // Initialize the Waits class
    }

    // Click on the provided WebElement after ensuring it's clickable
    public void click(WebElement element) {
        waits.waitForElementToBeClickable(element);  // Wait until the element is clickable
        element.click();  // Perform the click action
    }

    // Select a dropdown option by its visible text
    public void selectByVisibleText(WebElement element, String text) {
        waits.waitForElementToBeVisible(element);  // Wait until the element is visible
        new Select(element).selectByVisibleText(text);  // Select the option by visible text
    }

    // Clear the text of the element and then type the provided text character by character
    public void clearAndType(WebElement element, String text) {
        try {
            waits.waitForElementToBeVisible(element);  // Wait until the element is visible
            highlightElement(element);  // Highlight the element (for visual indication)

            element.clear();  // Clear the existing text in the input field
            System.out.println("in clear fun");

            // Type the text character by character with a small delay (50ms)
            for (char ch : text.toCharArray()) {
                element.sendKeys(String.valueOf(ch));
                Thread.sleep(50);  // Small delay for typing effect
            }
        } catch (Exception e) {
            // Handle any exceptions that might occur and throw a runtime exception
            throw new RuntimeException("Failed to clear and type text: " + e.getMessage());
        }
    }

    // Highlight the element by changing its style temporarily
    private void highlightElement(WebElement element) {
        String originalStyle = element.getAttribute("style");  // Store the original style

        // Change the element's border and background color to highlight it
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].setAttribute('style', arguments[1]);",
                element,
                "border: 3px solid red; background-color: yellow;"
        );

        try {
            Thread.sleep(500);  // Keep the highlight for 500ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Handle interrupted exception
        } finally {
            // Restore the original style after the highlight
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    element,
                    originalStyle
            );
        }
    }
}
