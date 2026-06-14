package common;

import java.time.Duration;

import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static common.ContextSteps.appiumDriver;
import static org.junit.Assert.assertEquals;

public class CommonSteps {
	public WebDriver driver;
	WebDriverWait wait = null;
	CommonFunctions commonFunctions = new CommonFunctions();

	public CommonSteps() {

	}

	@When("I open web page")
	public void i_open_web_page() throws Throwable {
		try {
			System.out.println("I open web page");

			commonFunctions.funcReadPropertiesFile("src/main/resources/data.properties");
			commonFunctions.funcReadPropertiesFile("src/main/resources/errorMessage.properties");

			String baseURL = System.getProperty("baseUrl");

			if (driver == null) {
				ContextSteps contextSteps = new ContextSteps();
				driver = contextSteps.getDriver();
			}

			// Open URL
			driver.get(baseURL);

			wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(System.getProperty("objectTimeout"))));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@When("I navigate to web page")
	public void i_navigate_to_web_page() throws Throwable {
		try {
			System.out.println("I navigate to web page");

			String baseURL = System.getProperty("baseUrl");

			if (driver == null) {
				ContextSteps contextSteps = new ContextSteps();
				driver = contextSteps.getDriver();
			}

			// Open URL
			driver.get(baseURL);

			wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(System.getProperty("objectTimeout"))));

		} catch (Exception ex) {
			Assert.fail(ex.getMessage());
		}
	}

	@Then("I verify title page is {string}")
	public void i_verify_title_page_is(String strTitle) {
		try {
			System.out.println("I verify title page is " + strTitle);

			Thread.sleep(Integer.parseInt(System.getProperty("timeSleep").trim()));

			if (System.getProperty("osName").trim().toLowerCase().contains("windows")
					|| System.getProperty("osName").trim().toLowerCase().contains("mac")) {

				assertEquals("Expect result: " + strTitle + " - Actual result: " +driver.getTitle(), strTitle.trim().toLowerCase(), driver.getTitle().trim().toLowerCase());
			}

			if (System.getProperty("osName").trim().toLowerCase().contains("android")
					|| System.getProperty("osName").trim().toLowerCase().contains("ios")) {

				assertEquals(strTitle.trim().toLowerCase(), appiumDriver.getTitle().trim().toLowerCase());
			}

		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

}

