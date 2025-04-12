package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

public class AddressPage extends BasePage {

    @FindBy(id = "address-ui-widgets-enterAddressFullName")
    private WebElement fullNameField;

    @FindBy(id = "address-ui-widgets-enterAddressPhoneNumber")
    private WebElement phoneNumberField;

    @FindBy(id = "address-ui-widgets-enterAddressLine1")
    private WebElement streetAddressField;

    @FindBy(id = "address-ui-widgets-enterAddressCity")
    private WebElement cityInput;

    @FindBy(id = "address-ui-widgets-enterAddressDistrictOrCounty")
    private WebElement districtInput;

    @FindBy(id = "address-ui-widgets-enter-building-name-or-number")
    private WebElement buildingNumber;

    @FindBy(id = "checkout-primary-continue-button-id")
    private WebElement submitAddressButton;

    /**
     * Adds a new address using the provided details.
     */
    public void addNewAddress(String fullName, String phoneNumber, String city, String district, String street, String buildingNum) {
        try {
            safeSendKeys(fullNameField, fullName);
            safeSendKeys(phoneNumberField, phoneNumber);
            safeSendKeys(streetAddressField, street);

            selectFromInputField(cityInput, city);
            selectFromInputField(districtInput, district);
            selectFromInputField(buildingNumber, buildingNum);

            reliableClick(submitAddressButton);

            Thread.sleep(7000); // Temporary wait (replace with explicit wait if possible)

        } catch (Exception e) {
            throw new RuntimeException("Failed to add new address", e);
        }
    }

    /**
     * Waits for visibility, clears, and sends keys to input.
     */
    private void safeSendKeys(WebElement element, String text) {
        waits.waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Sends input to a field and confirms with TAB.
     */
    private void selectFromInputField(WebElement inputField, String value) {
        waits.waitForElementToBeClickable(inputField);
        inputField.clear();
        inputField.sendKeys(value);
        inputField.sendKeys(Keys.TAB);
    }

    /**
     * Scrolls to the element and clicks it reliably.
     */
    private void reliableClick(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});",
                    element
            );
            element.click();

        } catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();", element
            );
        }
    }
}
