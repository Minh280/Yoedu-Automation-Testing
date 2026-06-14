package pageObject;

import common.CommonFunctions;
import io.appium.java_client.AppiumDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class YoeduLoginPage {
    WebDriver driver;
    AppiumDriver appiumDriver;

    // Khai báo Locators bằng By (Theo phong cách của bạn)
    By txtUsername = By.xpath("//input[@name='email']");
    By txtPassword = By.xpath("//input[@name='password']");
    By btnLogin = By.xpath("//button[contains(., 'Đăng nhập')]");

    // --- Locators Thông báo lỗi ---

    By lblFieldError = By.xpath("//p[contains(@class, 'MuiFormHelperText-root') and contains(@class, 'Mui-error')]");

    // By lblToastError  = By.xpath("//div[contains(@class, 'MuiAlert-message')]");

    CommonFunctions commonFunction = new CommonFunctions();

    // Lấy timeout từ system property tương tự mẫu bạn cung cấp
    int intTimeOut = Integer.parseInt(System.getProperty("objectTimeout", "20").trim());

    public YoeduLoginPage(WebDriver driver) {
        if (System.getProperty("osName").toLowerCase().contains("android")) {
            this.appiumDriver = (AppiumDriver) driver;
        } else {
            this.driver = driver;
        }
    }

    public void login(String username, String password) {
        try {
            String os = System.getProperty("osName").trim().toLowerCase();

            if (os.contains("windows") || os.contains("mac")) {
                commonFunction.waitUntilElementLocated(driver, txtUsername, intTimeOut);
                driver.findElement(txtUsername).sendKeys(username);

                driver.findElement(txtPassword).sendKeys(password);

                driver.findElement(btnLogin).click();
            }
            //else if (os.contains("android")) {
                // Đợi và thao tác trên Android qua AppiumDriver
                //commonFunction.waitUntilElementLocated(appiumDriver, txtUsername, intTimeOut);
                //appiumDriver.findElement(txtUsername).sendKeys(username);
                //appiumDriver.findElement(txtPassword).sendKeys(password);
                //appiumDriver.findElement(btnLogin).click();
            //}
        } catch (Exception e) {
            Assert.fail("Lỗi tại YoeduLoginPage: " + e.getMessage());
        }
    }

    public void navigateToLogin(String url) {
        try {
            if (driver != null) {
                driver.get(url);
            } else {
                appiumDriver.get(url);
            }
        } catch (Exception e) {
            Assert.fail("Không thể mở trang đăng nhập: " + e.getMessage());
        }
    }
    public void inputCredentials(String username, String password) {
        commonFunction.waitUntilElementLocated(driver, txtUsername, intTimeOut);

        // Xóa sạch dữ liệu cũ đề phòng trường hợp Examples chạy lặp lại
        driver.findElement(txtUsername).clear();
        driver.findElement(txtUsername).sendKeys(username);

        driver.findElement(txtPassword).clear();
        driver.findElement(txtPassword).sendKeys(password);
    }

    public void clickLoginButton() {
        driver.findElement(btnLogin).click();
    }

    public String getActualErrorMessage() {
        try {
            // Kiểm tra xem thẻ báo lỗi có tồn tại trên giao diện hay không
            if (!driver.findElements(lblFieldError).isEmpty()) {
                return driver.findElement(lblFieldError).getText().trim();
            }
        } catch (Exception e) {
            System.out.println("Không thể lấy thông báo lỗi: " + e.getMessage());
        }
        return ""; // Trả về chuỗi rỗng nếu hệ thống không hiển thị lỗi nào
    }
}

