package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utilities.ConfigReader;

import java.time.Duration;

public class HomePage extends BasePage {

    protected WebDriverWait wait;

    // Page Elements
    @FindBy(id = "nav-link-accountList")
    private WebElement accountLink;

    @FindBy(id = "nav-hamburger-menu")
    private WebElement allMenuButton;

    @FindBy(id = "nav-logo-sprites")
    private WebElement amazonLogo;

    @FindBy(css = "#hmenu-canvas")
    private WebElement menuPanel;

    @FindBy(xpath = "//a[@aria-label='See All Categories']")
    private WebElement seeall;

    @FindBy(xpath = "//a[@class='hmenu-item' and @data-menu-id='16']")
    private WebElement videoGamesLink;

    @FindBy(xpath = "//a[contains(text(),'All Video Games')]")
    private WebElement allVideoGamesLink;

    // Constructor to initialize elements and wait
    public HomePage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Navigate to Amazon home page using the URL from config file
    public HomePage navigateToHomePage() {
        String url = ConfigReader.getUrl();
        if (url == null || url.isEmpty()) {
            throw new RuntimeException("URL cannot be null or empty - check config.properties");
        }

        driver.get(url);
        waits.waitForPageToLoad();
        verifyPageLoaded();
        return this;
    }

    // Verify that the homepage is loaded by checking logo and title/URL
    private void verifyPageLoaded() {
        waits.waitForElementToBeVisible(amazonLogo, Duration.ofSeconds(10));
        String currentUrl = driver.getCurrentUrl();
        String currentTitle = driver.getTitle();

        if (!currentUrl.contains("amazon.eg") && !currentTitle.contains("Amazon.eg")) {
            throw new AssertionError("Not on Amazon Egypt homepage. Current URL: " + currentUrl +
                    ", Title: " + currentTitle);
        }
    }

    // Click on account link to go to the login page
    public void goToLoginPage() {
        try {
            waits.waitForElementToBeClickable(accountLink);
            elementActions.click(accountLink);
        } catch (Exception e) {
            throw new RuntimeException("Failed to navigate to login page", e);
        }
    }

    // Open the main hamburger menu
    public void openAllMenu() {
        try {
            waits.waitForElementToBeVisible(allMenuButton);
            elementActions.click(allMenuButton);
            waits.waitForElementToBeVisible(menuPanel);
            Assert.assertTrue(menuPanel.isDisplayed(), "Menu panel did not open properly");
            waits.waitForPageToLoad();
        } catch (Exception e) {
            Assert.fail("Failed to open all menu: " + e.getMessage());
        }
    }

    // Click on "See All Categories" inside the menu
    public void selectSeeall() {
        wait.until(ExpectedConditions.visibilityOf(seeall));
        wait.until(ExpectedConditions.elementToBeClickable(seeall)).click();
    }

    // Click on "Video Games" category inside the menu
    public void selectVideoGames() {
        wait.until(ExpectedConditions.visibilityOf(videoGamesLink));
        wait.until(ExpectedConditions.elementToBeClickable(videoGamesLink)).click();
    }

    // Click on "All Video Games" using JavaScript executor for better reliability
    public void selectAllVideoGames() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", allVideoGamesLink);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Check whether the user is logged in based on the account link text
    public boolean isUserLoggedIn() {
        try {
            return !accountLink.findElement(By.xpath("//*[@id=\"nav-link-accountList-nav-line-1\"]"))
                    .getText().toLowerCase().contains("sign in");
        } catch (Exception e) {
            return false;
        }
    }
}
