package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import utilities.TestDataProvider;
import utilities.Waits;

import java.io.IOException;

public class AmazonPurchaseTest extends BaseTest {

    @Test
    public void testAmazonPurchaseFlow() throws InterruptedException, IOException {

        // 1. Open Amazon and login
        HomePage homePage = new HomePage();
        homePage.navigateToHomePage();

        // If the user is not logged in, perform login
        if (!homePage.isUserLoggedIn()) {
            homePage.goToLoginPage();
            LoginPage loginPage = new LoginPage();
            loginPage.login(
                    TestDataProvider.getTestData("user.valid.email"),
                    TestDataProvider.getTestData("user.valid.password")
            );
            Assert.assertTrue(homePage.isUserLoggedIn(), "Login failed");
        }

        // 2. Open the "All" menu and navigate to "All Video Games"
        homePage.openAllMenu();
        homePage.selectSeeall();
        homePage.selectVideoGames();
        homePage.selectAllVideoGames();

        // 3. Apply filters and sort products by price (High to Low)
        VideoGamesPage videoGamesPage = new VideoGamesPage();
        videoGamesPage.applyFilters();
        videoGamesPage.sortByPriceHighToLow();

        // 4. Add products under 15k EGP
        double totalBelow15k = videoGamesPage.addProductsBelowPrice();

        // 5. Verify cart
        CartPage cartPage = new CartPage();
        // Optionally, verify if all products are added to the cart
        // Assert.assertTrue(cartPage.verifyAllProductsAdded(), "Not all products were added to the cart");

        // 6. Checkout process
        cartPage.click_checkout();
        cartPage.proceedToCheckout();
        cartPage.click_AddNewAddress();

        // 7. Add a new address
        AddressPage addressPage = new AddressPage();
        addressPage.addNewAddress(
                TestDataProvider.getTestData("address.shipping.fullName"),
                TestDataProvider.getTestData("address.shipping.phone"),
                TestDataProvider.getTestData("address.shipping.city"),
                TestDataProvider.getTestData("address.shipping.district"),
                TestDataProvider.getTestData("address.shipping.street"),
                TestDataProvider.getTestData("address.shipping.buildingNum")
        );

        // 8. Payment step: Select payment method (Cash on Delivery)
        PaymentPage paymentPage = new PaymentPage();
        paymentPage.selectCashOnDelivery();

        // 9. Verify total amount matches
        String expectedTotal = String.valueOf(totalBelow15k);
        String actualTotal = paymentPage.getTotalFromPaymentPage();

        // Assert that the expected and actual total amounts are the same
        Assert.assertEquals(actualTotal, expectedTotal, "Total amount doesn't match");
    }
}
