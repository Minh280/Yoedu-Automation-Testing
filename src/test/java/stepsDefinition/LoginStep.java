package stepsDefinition;

import common.ContextSteps;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And; // Thêm import cho @And
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObject.YoeduLoginPage;

import java.time.Duration;

public class LoginStep {
    WebDriver driver;
    AppiumDriver appiumDriver;
    WebDriverWait wait;
    YoeduLoginPage loginPage;

    public LoginStep() throws Throwable {
        ContextSteps contextSteps = new ContextSteps();
        String osName = System.getProperty("osName").toLowerCase();
        int timeout = Integer.parseInt(System.getProperty("objectTimeout", "20"));

        if (osName.contains("android") || osName.contains("mac")) {
            appiumDriver = (AppiumDriver) contextSteps.getDriver();
            loginPage = new YoeduLoginPage(appiumDriver);
            wait = new WebDriverWait(appiumDriver, Duration.ofSeconds(timeout));
        }
        else if (osName.contains("windows") || osName.contains("mac")) {
            driver = contextSteps.getDriver();
            loginPage = new YoeduLoginPage(driver);
            wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        }
    }

    // =================================================================
    // --- CÁC STEP CHO LUỒNG HAPPY CASE (Đã giữ nguyên logic của bạn) ---
    // =================================================================

    @Given("Tôi mở trang đăng nhập Yoedu")
    public void openLoginPage() throws Throwable {
        try {
            loginPage.navigateToLogin("https://management.yoedu.vn/login");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @When("Tôi thực hiện đăng nhập với tài khoản {string} và mật khẩu {string}")
    public void enterCredentials(String username, String password) throws Throwable {
        try {
            loginPage.login(username, password);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Then("Hệ thống đăng nhập thành công và không còn ở trang login")
    public void verifyLoginSuccess() throws Throwable {
        try {
            System.out.println("Kiểm tra trạng thái đăng nhập");
            Thread.sleep(2000); // Chờ trang chuyển hướng

            String currentUrl;
            if (driver != null) {
                currentUrl = driver.getCurrentUrl();
            } else {
                currentUrl = appiumDriver.getCurrentUrl();
            }

            Assert.assertFalse("Đăng nhập thất bại, vẫn đang ở trang login!", currentUrl.contains("/login"));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    // =================================================================
    // --- ĐÃ THÊM: CÁC STEP CHO LUỒNG UNHAPPY CASE (VALIDATION TEST) ---
    // =================================================================

    @When("Tôi thực hiện nhập tài khoản là {string} và mật khẩu là {string}")
    public void enterInvalidCredentials(String username, String password) throws Throwable {
        try {
            System.out.println("Thực hiện nhập dữ liệu test validation: User[" + username + "] - Pass[" + password + "]");
            // Gọi hàm điền thông tin credentials từ loginPage (chưa click nút Login)
            loginPage.inputCredentials(username, password);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @And("Tôi nhấn nút Đăng nhập")
    public void clickLoginBtn() throws Throwable {
        try {
            System.out.println("Nhấn nút Đăng nhập để kích hoạt kiểm tra validation");
            loginPage.clickLoginButton();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Then("Hệ thống phải hiển thị thông báo lỗi tương ứng là {string}")
    public void verifyErrorMessage(String expectedError) throws Throwable {
        try {
            System.out.println("Đang bắt thông báo lỗi trên giao diện...");
            Thread.sleep(1000); // Chờ hiệu ứng render text đỏ hoặc toast hiện lên

            // Lấy chuỗi thông báo lỗi thực tế từ Page Object
            String actualError = loginPage.getActualErrorMessage();

            System.out.println("Thông báo lỗi thực tế: [" + actualError + "]");
            System.out.println("Thông báo lỗi mong đợi: [" + expectedError + "]");

            // So sánh xem thông báo thực tế có chứa từ khóa mong đợi không
            Assert.assertTrue("LỖI: Thông báo lỗi hiển thị trên giao diện không khớp!",
                    actualError.contains(expectedError));

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}