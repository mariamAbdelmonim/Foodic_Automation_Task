package pages;

import base.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CartPage extends BasePage {

    @FindBy(name = "proceedToRetailCheckout")
    private WebElement proceedToCheckoutButton;

    @FindBy(linkText = "Add a new delivery address")
    private WebElement deliveryAddress;

    /**
     * Navigates to the cart page.
     */
    public void click_checkout() {
        driver.navigate().to("https://www.amazon.eg/cart");
        waits.waitForPageToLoad();
    }

    /**
     * Clicks on the link to add a new delivery address.
     */
    public void click_AddNewAddress() {
        elementActions.click(deliveryAddress);
        waits.waitForPageToLoad();
    }

    /**
     * Proceeds to checkout from the cart page.
     */
    public void proceedToCheckout() throws InterruptedException {
        waits.waitForElementToBeClickable(proceedToCheckoutButton);
        elementActions.click(proceedToCheckoutButton);
        waits.waitForPageToLoad();
        Thread.sleep(3000); // Optional: replace with dynamic wait
    }
}
