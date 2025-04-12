package pages;

import base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.time.Duration;

public class LoginPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    // Page Elements
    @FindBy(id = "ap_email_login")
    WebElement emailField;

    @FindBy(id = "continue")
    WebElement continueButton;

    @FindBy(id = "ap_password")
    WebElement passwordField;

    @FindBy(id = "signInSubmit")
    WebElement signInButton;

    @FindBy(css = ".a-alert-heading")
    private WebElement errorMessage;

    @FindBy(id = "nav-link-accountList-nav-line-1")
    private WebElement accountNameText;

    /**
     * Attempts to log in using the provided email and password.
     * Retries only once if the login fails due to a temporary issue.
     */
    public void login(String email, String password) throws InterruptedException {
        int maxAttempts = 1;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                logger.info("Login attempt #{}", attempt);
                enterEmail(email);
                enterPassword(password);

                if (isUserLoggedIn()) {
                    logger.info("Login successful");
                    return;
                }
            } catch (Exception e) {
                logger.error("Attempt {} failed: {}", attempt, e.getMessage());
                if (attempt == maxAttempts) throw e;
                waits.sleep(Duration.ofSeconds(2));
            }
        }

        Assert.fail("Login failed after " + maxAttempts + " attempts");
    }

    /**
     * Enters the user's email and clicks the Continue button.
     * Then waits for either the password field or error message.
     */
    private void enterEmail(String email) {
        elementActions.clearAndType(emailField, email);
        elementActions.click(continueButton);

        try {
            waits.waitForEither(
                    Duration.ofSeconds(5),
                    passwordField,
                    errorMessage
            );

            if (errorMessage.isDisplayed()) {
                throw new RuntimeException("Email error: " + errorMessage.getText());
            }

        } catch (NoSuchElementException e) {
            logger.warn("Unexpected page state after email entry");
        }
    }

    /**
     * Enters the password and clicks the Sign In button.
     */
    private void enterPassword(String password) {
        waits.waitForElementToBeVisible(passwordField, Duration.ofSeconds(5));
        elementActions.clearAndType(passwordField, password);
        elementActions.click(signInButton);
    }

    /**
     * Checks if the user is logged in based on the account label text.
     */
    public boolean isUserLoggedIn() {
        try {
            return !accountNameText.getText().contains("Hello, sign in");
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}
