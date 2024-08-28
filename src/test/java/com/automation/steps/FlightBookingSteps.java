package com.automation.steps;

import com.automation.pages.ConfirmationPage;
import com.automation.pages.FlightsPage;
import com.automation.pages.HomePage;
import com.automation.pages.PurchasePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.AssertJUnit.*;

public class FlightBookingSteps {

    WebDriver driver;
    HomePage homePage;
    FlightsPage flightsPage;
    PurchasePage purchasePage;
    ConfirmationPage confirmationPage;

    @Given("the user is on the BlazeDemo homepage")
    public void theUserIsOnTheBlazeDemoHomepage() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window.size=1920x1080");
        WebDriverManager.chromedriver()
                .capabilities(options).remoteAddress("seleniumhub:4444/wd/hub").create();
        driver.get("https://blazedemo.com/");
        homePage = new HomePage(driver);
    }

    @When("they select {string} as the departure city and they select {string} as the destination city")
    public void theySelectAsTheDepartureCityAndTheySelectAsTheDestinationCity(String fromCity, String toCity) {
        homePage = new HomePage(driver);
        homePage.selectFromAndToCity(fromCity, toCity);
    }

    @And("they search for flights")
    public void theySearchForFlights() {
        homePage.clickFindFlight();
    }

    @Then("they should see the flight results page")
    public void theyShouldSeeTheFlightResultsPage() {
        String pageTitle = driver.getTitle();
        assertEquals("BlazeDemo - reserve", pageTitle );
    }

    @When("they select the cheapest available flight")
    public void theySelectTheCheapestAvailableFlight() {
        flightsPage = new FlightsPage(driver);
        String result = flightsPage.selectCheapestFlight();
        assertEquals("- Cheapest Price: 200.98",result);
    }

    @And("they enter the purchase information name {string}, address {string}, city {string}, zipCode {string}")
    public void theyEnterThePurchaseInformation(String name,String address, String city, String zipCode) {
        purchasePage = new PurchasePage(driver);
        purchasePage.fillPurchaseFields(name, address, city, zipCode);
//        purchasePage.clickPurchaseFlight();
    }

    @Then("they should see a purchase confirmation with a success message")
    public void theyShouldSeeAPurchaseConfirmationWithASuccessMessage() {
        ConfirmationPage confirmationPage = new ConfirmationPage(driver);
        assertEquals("Thank you for your purchase today!", confirmationPage.getConfirmationMessage());
    }

    @And("they should see a booking ID")
    public void theyShouldSeeABookingID() {
        confirmationPage = new ConfirmationPage(driver);
        String bookingId = confirmationPage.getBookingId();
        assertNotNull(bookingId);
        assertFalse("El ID de reserva esta vació.", bookingId.isEmpty());
        driver.quit();
    }
}
