package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverManager {

    // ThreadLocal to ensure each thread (test) gets its own WebDriver instance
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Map to associate browser names with their corresponding initialization logic
    private static final Map<String, Runnable> browserMap = new HashMap<>();

    // Static block to initialize browser configurations
    static {
        // Setup Chrome browser configuration
        browserMap.put("chrome", () -> {
            WebDriverManager.chromedriver().setup();  // Set up ChromeDriver using WebDriverManager
            ChromeOptions options = new ChromeOptions();

            // Check if headless mode is configured and add it to options
            if (Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                options.addArguments("--headless=new");
            }

            // Initialize the ChromeDriver with the given options
            driver.set(new ChromeDriver(options));
        });

        // Setup Firefox browser configuration
        browserMap.put("firefox", () -> {
            WebDriverManager.firefoxdriver().setup();  // Set up FirefoxDriver using WebDriverManager
            FirefoxOptions options = new FirefoxOptions();

            // Check if headless mode is configured and add it to options
            if (Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                options.addArguments("--headless");
            }

            // Initialize the FirefoxDriver with the given options
            driver.set(new FirefoxDriver(options));
        });

        // Setup Edge browser configuration
        browserMap.put("edge", () -> {
            WebDriverManager.edgedriver().setup();  // Set up EdgeDriver using WebDriverManager
            EdgeOptions options = new EdgeOptions();

            // Check if headless mode is configured and add it to options
            if (Boolean.parseBoolean(ConfigReader.getProperty("headless"))) {
                options.addArguments("--headless=new");
            }

            // Initialize the EdgeDriver with the given options
            driver.set(new EdgeDriver(options));
        });
    }

    // Method to initialize the WebDriver based on the browser type
    public static void initializeDriver(String browser) {
        // Normalize the browser name to lower case for comparison
        String normalizedBrowser = browser.trim().toLowerCase();

        // Validate if the provided browser is supported
        if (!browserMap.containsKey(normalizedBrowser)) {
            throw new IllegalArgumentException(String.format(
                    "Unsupported browser: '%s'. Supported browsers: %s",
                    browser,
                    browserMap.keySet()
            ));
        }

        // Run the initialization logic for the selected browser
        browserMap.get(normalizedBrowser).run();

        // Maximize the browser window
        getDriver().manage().window().maximize();

        // Set implicit wait based on the configuration
        getDriver().manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getImplicitWait()));

        // Log the browser information
        System.out.println("Browser received: '" + browser + "'");
        System.out.println("Normalized: '" + normalizedBrowser + "'");
        System.out.println("Available browsers: " + browserMap.keySet());
    }

    // Method to retrieve the current WebDriver instance
    public static WebDriver getDriver() {
        return driver.get();
    }

    // Method to quit the WebDriver and clean up resources
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();  // Quit the current WebDriver session
            driver.remove();  // Remove the WebDriver instance from the thread-local storage
        }
    }
}
