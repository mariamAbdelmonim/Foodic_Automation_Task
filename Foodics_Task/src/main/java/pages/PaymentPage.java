package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.io.IOException;

public class PaymentPage extends BasePage {

    // Web element representing the Cash on Delivery payment option
    @FindBy(className = "pmts-instrument-selector")
    private WebElement cashOnDeliveryOption;

    // Web element representing the total price displayed on the payment page
    @FindBy(xpath = "//div[@class='order-summary-line-definition']//span[@class='aok-nowrap a-nowrap']")
    private WebElement TotalPriceInPayment;

    /**
     * Selects the "Cash on Delivery" payment method.
     */
    public void selectCashOnDelivery() {
        waits.waitForElementToBeVisible(cashOnDeliveryOption);
        elementActions.click(cashOnDeliveryOption);
    }

    /**
     * Gets the total amount text from the payment page, cleans it, and returns it as a formatted string.
     *
     * @return String representation of the total price (e.g., "77177.0")
     * @throws IOException if there's an issue reading the element (declared for potential future use)
     */
    public String getTotalFromPaymentPage() throws IOException {
        WebElement totalAmount = TotalPriceInPayment;
        String amountText = totalAmount.getText();

        // Remove currency symbols and non-numeric characters except for the decimal point
        String cleanedAmount = amountText.replaceAll("[^\\d.]", "");

        // Convert cleaned amount to a double
        double numericValue = Double.parseDouble(cleanedAmount);

        // Format to one decimal place
        return String.format("%.1f", numericValue);
    }

    /**
     * Gets the numeric value of the total amount from the order summary.
     * Verifies the currency is EGP or ج.م (Arabic EGP).
     *
     * @return double value of the total amount
     */
    public double getTotalAmount() {
        try {
            // Locators for amount and currency symbol
            By totalAmountLocator = By.cssSelector(".grand-total .a-price-whole");
            By currencySymbolLocator = By.cssSelector(".grand-total .a-price-symbol");

            // Wait for the amount to be visible
            waits.waitForElementToBeVisible((WebElement) totalAmountLocator);

            WebElement totalElement = driver.findElement(totalAmountLocator);
            WebElement currencyElement = driver.findElement(currencySymbolLocator);

            // Clean and parse the amount text
            String amountText = totalElement.getText().replaceAll("[^0-9.]", "");
            String currency = currencyElement.getText();

            // Validate currency is EGP
            if (!currency.equals("EGP") && !currency.equals("ج.م")) {
                Assert.fail("Expected currency EGP but found: " + currency);
            }

            return Double.parseDouble(amountText);

        } catch (NoSuchElementException e) {
            throw new RuntimeException("Total amount element not found on the page", e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to convert amount to numeric value", e);
        }
    }
}
