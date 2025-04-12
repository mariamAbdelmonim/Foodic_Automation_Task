package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import utilities.DriverManager;
import utilities.ElementActions;
import utilities.Waits;

/**
 * Abstract BasePage class that serves as the foundation for all page classes.
 * It initializes the WebDriver instance, common utility classes, and
 * sets up the PageFactory to manage WebElements.
 */
public abstract class BasePage {

    // WebDriver instance used to interact with the browser
    protected WebDriver driver;

    // Utility class for performing common element interactions (e.g., click, send keys)
    protected ElementActions elementActions;

    // Utility class for handling explicit waits (visibility, clickability, etc.)
    protected Waits waits;

    /**
     * Constructor that initializes the WebDriver, utility classes, and WebElements.
     */
    public BasePage() {
        // Obtain the WebDriver instance from DriverManager utility
        this.driver = DriverManager.getDriver();

        // Initialize utility classes for element actions and wait conditions
        this.elementActions = new ElementActions();
        this.waits = new Waits();

        // Initialize web elements using PageFactory and the current WebDriver instance
        PageFactory.initElements(driver, this);
    }
}
