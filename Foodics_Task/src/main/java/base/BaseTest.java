package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import utilities.ConfigReader;
import utilities.DriverManager;

/**
 * Base class for all test classes.
 */
public class BaseTest {

    @BeforeMethod
    public void setUp() {
        // Initialize WebDriver with the configured browser
        String browser = ConfigReader.getProperty("browser").trim();
        DriverManager.initializeDriver(browser);
    }

    @AfterMethod
    public void tearDown() {
        // Quit WebDriver after each test
        DriverManager.quitDriver();
    }

    @DataProvider(name = "browsers")
    public Object[][] browserProvider() {
        // Provide multiple browsers for cross-browser testing
        String[] browsers = ConfigReader.getProperty("browsers").split(",");
        Object[][] data = new Object[browsers.length][1];
        for (int i = 0; i < browsers.length; i++) {
            data[i][0] = browsers[i].trim();
        }
        return data;
    }
}
