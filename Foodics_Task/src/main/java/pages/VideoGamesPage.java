package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class VideoGamesPage extends BasePage {
    protected WebDriverWait wait;

    // ========== Filter Elements ==========
    @FindBy(id = "s-result-sort-select")
    private WebElement sortDropdown;

    @FindBy(xpath= "//i[contains(@class, 'a-icon-checkbox')]")
    private WebElement freeShippingFilter;

    @FindBy(xpath = "//span[contains(text(), 'New')]")
    private WebElement newConditionFilter;

    // ========== Navigation Elements ==========
    @FindBy(xpath = "//a[contains(@aria-label, 'Go to next page')]")
    private WebElement nextPageButton;

    // ========== Core Methods ==========

    // Constructor to initialize elements and WebDriverWait
    public VideoGamesPage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /**
     * Applies the filters for Free Shipping and New Condition.
     */
    public void applyFilters() {
        elementActions.click(freeShippingFilter);
        waits.waitForElementToBeClickable(newConditionFilter);
        elementActions.click(newConditionFilter);
        waits.waitForPageToLoad();
    }

    /**
     * Sorts the products by price in descending order.
     */
    public void sortByPriceHighToLow() {
        elementActions.selectByVisibleText(sortDropdown, "Price: High to Low");
        waits.waitForPageToLoad();
        verifySortingApplied();
    }

    /**
     * Verifies that the sorting option "Price: High to Low" is selected.
     */
    private void verifySortingApplied() {
        String selectedOption = new Select(sortDropdown).getFirstSelectedOption().getText();
        Assert.assertEquals(selectedOption, "Price: High to Low",
                "Products were not sorted correctly");
    }

    /**
     * Adds products with a price below the specified MAX_PRICE to the cart.
     *
     * @return Total price of all added products.
     * @throws InterruptedException if there's a delay in processing (e.g., page loading)
     */
    public double addProductsBelowPrice() throws InterruptedException {
        int addedProductsCount = 0;
        double totalProductsPrice = 0;
        final double MAX_PRICE = 15000.0;
        int currentPage = 1;
        final int MAX_PAGES = 2; // Maximum pages to be checked

        while (currentPage <= MAX_PAGES) {
            // Wait for product elements to be visible on the current page
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.cssSelector("div[data-component-type='s-search-result']")));
            List<WebElement> productsOnPage = driver.findElements(
                    By.cssSelector("div[data-component-type='s-search-result']"));

            if (productsOnPage.isEmpty()) {
                throw new AssertionError("No products found on page " + currentPage);
            }

            System.out.println("Page " + currentPage + " - Products found: " + productsOnPage.size());

            // Iterate through each product on the page
            for (int i = 0; i < productsOnPage.size(); i++) {
                WebElement product;
                try {
                    product = productsOnPage.get(i);
                    scrollToElement(product);

                    // Get price for each product using the getProductPrice method
                    double price = getProductPrice(product);
                    System.out.printf("Product %d - Price: %.2f%n", i + 1, price);

                    if (price < MAX_PRICE) {
                        // Try to add to cart if the product is below the price limit
                        if (addToCart(product)) {
                            addedProductsCount++;
                            totalProductsPrice += price;
                            System.out.println("Added - Price: " + price);
                            // Short wait between adding products
                            Thread.sleep(1000);
                        }
                    } else {
                        System.out.println("Skipped - Price exceeds limit");
                    }
                } catch (StaleElementReferenceException e) {
                    System.out.println("Stale element encountered. Skipping this product...");
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }

            System.out.println("Page " + currentPage + " completed. Total added so far: " + addedProductsCount);

            // Stop if we've reached the max pages
            if (currentPage >= MAX_PAGES) {
                System.out.println("Reached maximum pages limit (" + MAX_PAGES + ")");
                break;
            }

            // Move to next page if available
            if (isNextPageAvailable()) {
                goToNextPage();
                currentPage++;
                Thread.sleep(1500); // Wait for the new page to load
            } else {
                System.out.println("No more pages available");
                break;
            }
        }

        System.out.println("Total Products Price: " + totalProductsPrice);
        return totalProductsPrice;
    }

    // ========================= Helper Methods =========================

    /**
     * Scrolls the element into view smoothly.
     *
     * @param element WebElement to be scrolled into view
     */
    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior:'smooth',block:'center'});",
                element);
    }

    /**
     * Gets the price of a product from the product element.
     *
     * @param product WebElement representing the product
     * @return double price of the product
     */
    private double getProductPrice(WebElement product) {
        try {
            // Wait for the price element to become visible
            WebElement wholePart = wait.until(ExpectedConditions.visibilityOf(
                    product.findElement(By.cssSelector(".a-price-whole"))));
            WebElement fractionPart = wait.until(ExpectedConditions.visibilityOf(
                    product.findElement(By.cssSelector(".a-price-fraction"))));

            String whole = wholePart.getText().replaceAll("[^0-9]", "");
            String fraction = fractionPart.getText().replaceAll("[^0-9]", "");

            if (whole.isEmpty() || fraction.isEmpty()) {
                throw new RuntimeException("Invalid price format");
            }
            return Double.parseDouble(whole + "." + fraction);
        } catch (NoSuchElementException | TimeoutException e) {
            throw new RuntimeException("Price elements not found or not visible in time.", e);
        }
    }

    /**
     * Adds the product to the shopping cart using the product's own add button.
     *
     * @param product WebElement representing the product
     * @return true if the product was added to the cart, false otherwise
     */
    private boolean addToCart(WebElement product) {
        try {
            // Find and click the "Add to Cart" button inside the product element
            WebElement addButton = product.findElement(By.xpath(".//button[@name='submit.addToCart']"));
            scrollToElement(addButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);
            return true;
        } catch (Exception e) {
            System.out.println("Add to cart failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the next page button is available for navigation.
     *
     * @return true if the next page button is available, false otherwise
     */
    public boolean isNextPageAvailable() {
        try {
            return nextPageButton.isDisplayed() && nextPageButton.isEnabled();
        } catch (NoSuchElementException e) {
            System.out.println("No next page available.");
            return false;
        }
    }

    /**
     * Navigates to the next page by clicking the next page button.
     */
    public void goToNextPage() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                    nextPageButton);
            nextPageButton.click();
            // Wait until the next page is loaded
            wait.until(ExpectedConditions.stalenessOf(nextPageButton));
        } catch (Exception e) {
            System.out.println("Failed to navigate to next page: " + e.getMessage());
        }
    }
}
